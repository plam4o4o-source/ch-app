# Архитектура

## Слоеве и модули

```
┌──────────────────────────── :app (Android) ────────────────────────────┐
│  ui/          Compose екрани, ViewModel-и, навигация, тема, компоненти  │
│  notifications/  WorkManager: фонова синхронизация, напомняния         │
│  data/local/  файлов кеш, Room (любими, напомняния), DataStore          │
│  data/security/  Android Keystore (AES-256-GCM)                         │
│  network/     OkHttp (само TLS), наблюдение на връзката                 │
│  di/          AppContainer (ръчно DI) + FlavorServices (dev/prod)       │
└──────────────────────────────┬──────────────────────────────────────────┘
                               │ зависи от
┌──────────────────────────── shared (чист Kotlin/JVM) ──────────────────┐
│  domain/model       модели: NewsArticle, Event, CatalogBook, Loan, …   │
│  domain/service     интерфейси: CatalogService, NewsService, …         │
│  domain/repository  PayloadCache, SessionStore, SelfCardStore          │
│  data/site          chyavorec.org (Vercel): news.json, index.json, …   │
│  data/catalog       katalog.json: парсер, търсачка в паметта           │
│  data/invlib        InvLib: RemoteInvLibClient (предложен API),        │
│                     UnavailableInvLibServices (production по подразб.) │
│  data/repository    репозитори: мрежа → кеш, сесии, заемания           │
│  core               Outcome/AppError, Synced, часовник, дати, текст    │
└─────────────────────────────────────────────────────────────────────────┘
```

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
| `AuthenticationService`, `ReaderService`, `MembershipService` | `UnavailableInvLibServices` (prod) / `DemoInvLibServices` (само dev) | `RemoteInvLibClient` — вече готов, включва се с `INFLIB_API_URL` |

Смяната става само в `AppContainer` — репозиториите и UI не се променят.

## Репозитории

| Репозитори | Отговорност |
|---|---|
| `NewsRepository` | новини + пълни статии, кеш, ограничение на честотата (5 мин.) |
| `EventsRepository` | годишният календар на сайта (превърта се към следващото настъпване) |
| `CatalogRepository` | каталогът в паметта + суровият JSON на диска (офлайн търсене) |
| `SiteRepository` | раздели, страници, контакти, галерия, документи, индекс за търсене, празник на деня |
| `AuthRepository` | вход, подновяване на токена, изход (изтрива личния кеш), защита от налучкване |
| `ProfileRepository`, `LibraryRepository`, `MembershipRepository` | читателски данни през валидна сесия; шифрован кеш |
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

## Сигурност

| Мярка | Къде |
|---|---|
| Само HTTPS, без cleartext, само системни CA | `network_security_config.xml`, `ConnectionSpec.MODERN_TLS` |
| Токени, ръчна карта и читателски кеш — AES-256-GCM с ключ от Android Keystore | `KeystoreCipher`, `SecureSessionStore`, `FilePayloadCache(cipher)` |
| Паролата никога не се пази; `CharArray` се зачиства след заявката | `LoginViewModel`, `RemoteInvLibClient.login` |
| „Не ме помни“ → сесия само в паметта | `SecureSessionStore.save(persist = false)` |
| Изтичане на токена → refresh; отказ → изход и изтриване на личния кеш | `AuthRepository.validSession` |
| Защита от налучкване (5 опита → 60 s, удвояване) | `LoginThrottle` |
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

## Тестове

| Вид | Къде | Какво покрива |
|---|---|---|
| Unit | `shared/src/test` | дати, нормализиране на текст, URL, сроковете, членство |
| Парсери | `SiteParsingTest`, `KatalogParserTest` | реалните news.json, rss.xml, index.json, files.json, страниците „За нас“ и „Контакти“, реален katalog.json |
| Търсене | `CatalogSearchEngineTest` | всички полета, филтри, сортиране, 15 000 записа < 200 ms |
| Repository | `RepositoryTest`, `AuthRepositoryTest` | офлайн fallback, ограничение на честотата, сесии, refresh, изход, brute-force |
| API | `HttpServicesTest` (MockWebServer) | резервен източник на каталога, данните на сайта, предложеният InvLib API, грешки |
| Сигурно съхранение | `app/.../SecureStorageTest` | шифроване на диска, повреден кеш, сесия без запомняне |
| ViewModel | `app/.../ViewModelTest` | debounce търсене, филтри на новини, любими, офлайн грешка |
| UI (Compose + Robolectric) | `app/.../MainFlowsUiTest` | начален екран от сайта, търсене в каталога → книга, гост → ръчна карта, демо вход → „Моите книги“ |
