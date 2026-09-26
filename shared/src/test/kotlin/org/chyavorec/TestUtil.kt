package org.chyavorec

object TestUtil {
    fun resource(name: String): String =
        requireNotNull(javaClass.classLoader.getResource(name)) { "missing $name" }.readText()
}
