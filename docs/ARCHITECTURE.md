# Архитектура

## Слоеве и модули

```
┌──────────────────────────── :app (Android) ────────────────────────────┐
│  ui/          Compose екрани, ViewModel-и, навигация, тема, компоненти  │
│  notifications/  WorkManager: обща фонова задача, напомняния, известия  │
│  messages/    съобщенията от читалището: MessageCenter, проверка 1–6 ч. │
│  data/local/  файлов кеш, Room (любими, напомняния; FTS индекс на       │
│               каталога), DataStore                                      │
│  data/security/  Android Keystore (AES-256-GCM)                         │
│  network/     OkHttp (само TLS), наблюдение на връзката                 │
│  update/      самообновяване от GitHub Releases (само prodRelease)      │
│  widget/      Glance уиджет; diagnostics/ — файлът на дневника          │
│  di/          AppContainer (ръчно DI) + FlavorServices (dev/prod)       │
└──────────────────────────────┬──────────────────────────────────────────┘
                               │ зависи от
┌──────────────────────────── shared (чист Kotlin/JVM) ──────────────────┐
│  domain/model       модели: NewsArticle, Event, CatalogBook, Loan, …   │
│  domain/service     интерфейси: CatalogService, NewsService, …         │
│  domain/repository  PayloadCache, SessionStore, SelfCardStore          │
│  data/site          chyavorec.org (Vercel): news.json, index.json, …   │
│  data/catalog       katalog.json: парсер, търсачка в паметта           │
│  data/invlib        мостът към InvLib: RemoteInvLibClient,             │
│                     UnavailableInvLibServices (без INFLIB_API_URL)     │
│  data/repository    репозитори: мрежа → кеш, сесии, заемания           │
│  core               Outcome/AppError, Synced, часовник, дати, текст,   │
│                     локален дневник на грешките (Diagnostics)          │
└─────────────────────────────────────────────────────────────────────────┘
```

`:baselineprofile` — Macrobenchmark генератор на Baseline/Startup профила
(`Journeys.kt`); резултатът е в `app/src/main/generated/baselineProfiles/`.

`shared` е отделен Gradle build (`includeBuild("shared")`), затова може да се
компилира и тества **без Android SDK** (`cd shared && ../gradlew test`).

Пакетите съответстват на заданието: `core`, `data`, `domain`,
`presentation`/`ui`, `network`, `database` (`data/local`), `auth`
(`AuthRepository` + `SecureSessionStore`), `catalog`, `library`
(`LibraryRepository`), `news`, `events`, `profile`, `membership`,
`notifications`, `settings`.

## Интерфейси на услугите (заменяеми реализации)

| Интерфейс | Реализация днес | Бъдеща реализация |
|---|---|---|
| `CatalogService` | `GitHubCatalogService` (raw.githubusercontent → jsDelivr) | онлайн API на InvLib |
| `NewsService`, `EventsService`, `SiteContentService` | `ChyavorecSiteService` (JSON данните на сайта във Vercel + страници) | динамичен API на сайта, ако бъде добавен |
| `AuthenticationService`, `ReaderService`, `MembershipService` | `RemoteInvLibClient` (мостът, `INFLIB_API_URL`); без адрес — `UnavailableInvLibServices`; `DemoInvLibServices` (само dev) | същият договор директно от InvLib |

Смяната става само в `AppContainer` — репозиториите и UI не се променят.

## Репозитории

| Репозитори | Отговорност |
|---|---|
| `NewsRepository` | новини + пълни статии, кеш, ограничение на честотата (5 мин.) |
| `EventsRepository` | годишният календар на сайта (превърта се към следващото настъпване) |
| `CatalogRepository` | `katalog.json` (суровият JSON на диска за офлайн) + пълнотекстов индекс в Room (FTS4); при грешка на базата — индекс в паметта |
| `SiteRepository` | раздели, страници, контакти, галерия, документи, индекс за търсене, празник на деня |
| `AuthRepository` | вход, подновяване на токена, изход (изтрива личния кеш), защита от налучкване (`AuthRepository.kt`) |
| `ReaderDataRepository` | общото хранилище на читателските данни: една заявка `/v1/me/all` (ако мостът я има), свежест по вид, шифрован кеш по читател |
| `ProfileRepository`, `LibraryRepository`, `MembershipRepository`, `ReaderMessagesRepository` | тънки репозиторита по екрани върху `ReaderDataRepository` (`ReaderRepositories.kt`) |
| `SelfCardRepository` | ръчно въведената карта (Code 39 валидиране) |

### Кеш и офлайн режим

`CachedResource` прилага едно правило навсякъде:

1. успех от мрежата → запис в кеша, `fromCache = false`;
2. неуспех → последните данни с `fromCache = true`, `syncedAt` и причината;
3. няма кеш → грешка (UI показва съобщение и „Опитай отново“).

UI показва `SyncBanner` („Няма интернет връзка. Показваме последно наличните
данни.“ + „Последна синхронизация: преди 2 часа“), а за наличност на книги,
заемания и членство — винаги и датата на данните. Заеманията от кеша са
отбелязани „може да не са актуални“ и бутонът „Поднови“ е скрит.

## Представяне (UI)

- **MVVM**: всеки екран има ViewModel със `StateFlow` на неизменимо състояние.
- `ScreenState<T>` — един модел за skeleton / съдържание / празно / грешка /
  опресняване / синхронизация; `StateContent` го рисува еднакво навсякъде.
- **Навигация**: Navigation Compose, долна лента с 5 раздела (Начало, Каталог,
  Моето, Новини, Още), анимирани преходи (fade/scale за разделите, slide за
  вътрешните екрани).
- **Компоненти**: `BookCover` (генерирана корица в цвета на УДК раздела),
  `LibraryCard` (карта в размер ID-1 с Code 39), `ContentBlocksView` (native
  рисуване на съдържание от сайта), `PressableCard` (micro-interaction),
  shimmer skeleton, pull-to-refresh.
- **Анимации**: кратко въведение с логото (пръстен + появяване на името) след
  системния splash; въртяща се лента с новини с parallax; shared element
  преходи (корица на книга, снимка на новина) между списък и детайли;
  поетапно появяване на елементите; „подскачащи“ икони в долната навигация;
  анимирани броячи. Всички анимации се изключват при системната настройка
  „Премахване на анимациите“.
- **Достъпност**: семантични заглавия, `contentDescription`, live regions за
  грешки/офлайн, статусите имат текст (не само цвят), мащабируем текст (sp),
  touch targets ≥ 48 dp, shimmer уважава „Премахване на анимациите“.

## Фонова работа

| Задача | Период | Какво |
|---|---|---|
| `PeriodicSyncWorker` | ~6 ч. | лични съобщения; новини и заемания на ~12 ч. (ако са включени); събития — само за уиджета; нова версия на ~24 ч. |
| `MessagePollWorker` | буди се ~1 ч., пита на 1–6 ч. (`MessagePollPolicy`) | общите съобщения от `/data/app-messages.json` |
| `LoanReminderWorker`, `EventReminderWorker` | еднократно | локални напомняния (без мрежа) |

Всички периодични задачи искат интернет и „батерията не е изтощена“ и не питат мрежата,
ако резултатът не трябва на никого (изключени известия, няма уиджет, няма вход).

## Сигурност

| Мярка | Къде |
|---|---|
| Само HTTPS, без cleartext, само системни CA | `network_security_config.xml`, `ConnectionSpec.MODERN_TLS` |
| Токени, ръчна карта и читателски кеш — AES-256-GCM с ключ от Android Keystore | `KeystoreCipher`, `SecureSessionStore`, `FilePayloadCache(cipher)` |
| Паролата никога не се пази; `CharArray` се зачиства след заявката | `LoginViewModel`, `RemoteInvLibClient.login` |
| „Не ме помни“ → сесия само в паметта | `SecureSessionStore.save(persist = false)` |
| Изтичане на токена → refresh; отказ → изход и изтриване на личния кеш | `AuthRepository.validSession` |
| Защита от налучкване (5 опита → 60 s, удвояване); оцелява при рестарт, не се нулира при изход | `LoginThrottle`, `DataStoreThrottleStore` |
| „Наш ли е адресът“ — точен хост на разчетения адрес, само `https`; чуждото — във външния браузър | `TrustedLinks` (`DeepLinks.forUrl`, `SitePageScreen`, `fetchPage`, `ScanCodes`) |
| Обновление само с проверена сума и същия подпис; неразчетен подпис = отказ | `AppUpdater.verifyApk`, `SigningCheck` |
| Ограничение на честотата на заявките към сайта/каталога | `CachedResource.minRefreshMillis` |
| Без архивиране в облака/прехвърляне на данни | `allowBackup=false`, `data_extraction_rules.xml` |
| Без логове с лични данни; R8 премахва `Log.d/i/v` в release | `proguard-rules.pro` |
| Без тайни в Git; конфигурация от среда/`.env` | `.env.example`, `.gitignore` |
| Production не може да ползва демо данни (кодът е само в `src/dev`) | `FlavorServices` |
| `INFLIB_API_URL` се приема само с `https://` | `AppConfig.inflibConfigured` |

## Build варианти

| Вариант | applicationId | Демо данни |
|---|---|---|
| `devDebug` / `devRelease` | `org.chyavorec.app.dev` | по `USE_MOCK_DATA` (по подразбиране `true`) |
| `prodDebug` / `prodRelease` | `org.chyavorec.app` | **никога** |
| `prodPlay` (AAB за Google Play) | `org.chyavorec.app` | **никога** |

Самообновяване (`AppUpdater`, стъпка в `PeriodicSyncWorker`, `UpdateInstallReceiver`) е
включено само в `prodRelease` (`BuildConfig.SELF_UPDATE`). `prodPlay` е
`release` + `src/play/AndroidManifest.xml`, който премахва
`REQUEST_INSTALL_PACKAGES`, `UPDATE_PACKAGES_WITHOUT_USER_ACTION` и receiver-а;
CI проверява, че ги няма в слетия манифест. Подробно — README, „Автоматично обновяване“.

## Тестове

| Вид | Къде | Какво покрива |
|---|---|---|
| Unit | `shared/src/test` | дати, празници, нормализиране на текст, URL, ISBN, сроковете, членство, фоновите правила, дневника |
| Парсери | `SiteParsingTest`, `KatalogParserTest` | реалните news.json, rss.xml, index.json, files.json, страниците „За нас“ и „Контакти“, реален katalog.json |
| Търсене | `CatalogSearchEngineTest`, `CatalogFtsSearchTest`, `CatalogFtsRepositoryTest` | всички полета, филтри, сортиране, FTS индексът срещу търсачката в паметта, 15 000 записа < 200 ms |
| Repository | `RepositoryTest`, `CachingTest`, `AuthRepositoryTest`, `LoginFlowsTest`, `ReaderDataTest`, `LoanRenewTest`, `ReaderMessagesTest` | офлайн fallback, ограничение на честотата, сесии, refresh, изход, brute-force, читателските данни |
| Обновяване | `UpdateCheckerTest`, `app/.../AppUpdaterTest` | update.json (строга проверка на адреса и SHA-256), по-нова версия/minSdk, отхвърляне на повреден или чужд файл, изключено извън prodRelease |
| API | `HttpServicesTest` (MockWebServer) | резервен източник на каталога, данните на сайта, предложеният InvLib API, грешки |
| Сигурно съхранение | `app/.../SecureStorageTest` | шифроване на диска, повреден кеш, сесия без запомняне |
| ViewModel | `app/.../ViewModelTest`, `HomeViewModelTest`, `LoansViewModelTest`, `MessagesViewModelTest` | debounce търсене, филтри на новини, любими, офлайн грешка, заемания, съобщения |
| Фонова работа | `app/.../PeriodicSyncWorkerTest`, `MessagePollWorkerTest`, `LoanReminderWorkerTest` … | стъпките на задачите, известията, напомнянията |
| База | `app/.../AppDatabaseMigrationTest`, `CatalogIndexStoreTest` | миграциите срещу `app/schemas/`, FTS индексът |
| UI (Compose + Robolectric) | `app/.../MainFlowsUiTest`, `TabNavigationTest`, `DeepLinksTest` | начален екран от сайта, търсене в каталога → книга, гост → ръчна карта, демо вход → „Моите книги“, навигация, връзки |
| Снимки на екраните | `app/.../ScreenshotTest`, `LargeFontScreenshotTest`, `VisualsScreenshotTest`, `HolidayDecorScreenshotTest`, `PlayScreenshotTest` | PNG в клона `screenshots` при всеки push; за Play — `store/screenshots/` |
