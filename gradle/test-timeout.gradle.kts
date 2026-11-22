// Тест, който виси, не чака края на цялата задача в CI (60 мин): ако тестова JVM („Gradle Test
// Executor N“) няма напредък — започнат или завършен тест — повече от testHangTimeoutMinutes
// (5 мин), в лога излизат тестът, който върви, и thread dump на JVM-а (jcmd Thread.print), а
// JVM-ът се спира и задачата пада веднага. JUnit 4 няма общ timeout за всички тестове, а
// правилото Timeout пуска теста в друга нишка (Robolectric и Compose тестовете искат главната).
// Подстраховка — timeout на цялата задача. Прилага се в app и shared (apply(from = …)).

import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

val testHangTimeoutMinutes = providers.gradleProperty("testHangTimeoutMinutes").map { it.toLong() }.getOrElse(5L)

tasks.withType<Test>().configureEach {
    timeout.set(Duration.ofMinutes(20))
    addTestListener(TestHangWatchdog(path, testHangTimeoutMinutes))
}

class TestHangWatchdog(private val taskPath: String, private val limitMinutes: Long) : TestListener {
    private class Executor(@Volatile var since: Long, @Volatile var running: String?, @Volatile var finished: String?)

    // Състоянието е само по време на изпълнението (configuration cache не го пази).
    @Transient private var executors: ConcurrentHashMap<String, Executor>? = null
    @Transient private var timer: ScheduledExecutorService? = null

    private val log get() = org.gradle.api.logging.Logging.getLogger(TestHangWatchdog::class.java)

    // Дървото на Gradle: „Gradle Test Run :task“ → „Gradle Test Executor N“ → клас → тест.
    private fun executorOf(d: TestDescriptor): String? {
        var e = d
        while (e.parent?.parent != null) e = e.parent!!
        return if (e.parent != null) e.name else null
    }

    private fun nameOf(d: TestDescriptor) = if (d.className != null) "${d.className}.${d.name}" else d.name

    @Synchronized
    private fun start() {
        if (timer != null) return
        executors = ConcurrentHashMap()
        timer = Executors.newSingleThreadScheduledExecutor { r -> Thread(r, "test-hang-watchdog").apply { isDaemon = true } }
            .also { it.scheduleWithFixedDelay({ runCatching { check() }.onFailure { e -> log.warn("test-hang-watchdog: $e") } }, 15, 15, TimeUnit.SECONDS) }
    }

    @Synchronized
    private fun stop() {
        timer?.shutdownNow()
        timer = null
    }

    private fun progress(d: TestDescriptor, update: Executor.() -> Unit) {
        val name = executorOf(d) ?: return
        val map = executors ?: return
        map.getOrPut(name) { Executor(System.currentTimeMillis(), null, null) }.apply {
            update()
            since = System.currentTimeMillis()
        }
    }

    override fun beforeSuite(suite: TestDescriptor) {
        if (suite.parent == null) start() else progress(suite) {}
    }

    override fun afterSuite(suite: TestDescriptor, result: TestResult) {
        when {
            suite.parent == null -> stop()
            suite.parent?.parent == null -> executors?.remove(suite.name)
            else -> progress(suite) {}
        }
    }

    override fun beforeTest(test: TestDescriptor) = progress(test) { running = nameOf(test) }

    override fun afterTest(test: TestDescriptor, result: TestResult) = progress(test) {
        running = null
        finished = nameOf(test)
    }

    private fun check() {
        val map = executors ?: return
        val now = System.currentTimeMillis()
        for ((name, e) in map) {
            if (now - e.since < TimeUnit.MINUTES.toMillis(limitMinutes)) continue
            map.remove(name)
            val what = e.running?.let { "тестът $it не завърши за $limitMinutes мин" }
                ?: "няма напредък от $limitMinutes мин (последен завършен тест: ${e.finished ?: "няма"})"
            val prefix = if (System.getenv("GITHUB_ACTIONS") == "true") "::error::" else ""
            log.error("${prefix}$taskPath ($name): $what — thread dump и спиране на JVM-а на теста")
            val workers = ProcessHandle.current().descendants()
                .filter { p -> p.info().arguments().map { it.lastOrNull()?.trim('\'') == name }.orElse(false) }
                .toList()
            if (workers.isEmpty()) log.error("test-hang-watchdog: няма процес за $name")
            for (p in workers) {
                val jcmd = p.info().command().map { java.io.File(it).resolveSibling("jcmd") }
                    .filter { it.canExecute() }.map { it.path }.orElse("jcmd")
                runCatching {
                    val out = java.io.File.createTempFile("thread-dump", ".txt")
                    val proc = ProcessBuilder(jcmd, p.pid().toString(), "Thread.print").redirectErrorStream(true).redirectOutput(out).start()
                    if (!proc.waitFor(30, TimeUnit.SECONDS)) proc.destroyForcibly()
                    log.error("Thread dump на $name (pid ${p.pid()}):\n${out.readText()}")
                    out.delete()
                }.onFailure { log.error("test-hang-watchdog: jcmd: $it") }
                p.destroy()
                if (!p.onExit().runCatching { get(10, TimeUnit.SECONDS) }.isSuccess) p.destroyForcibly()
            }
        }
    }
}
