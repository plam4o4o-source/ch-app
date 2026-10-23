# Читалище Яворец — Android приложение

Официалното мобилно приложение на **НЧ „Васил Левски – 1922“, с. Яворец**:
читалище + библиотека + каталог + читателска карта + новини + събития в едно
native Android приложение.

- Съдържанието идва **живо** от сайта [chyavorec.org](https://chyavorec.org/)
  (статичен сайт във Vercel: `/data/news.json`, `/javora/index.json`,
  `/data/files.json`, `/api/calendar` и страниците) — нищо не се въвежда два пъти.
- Библиотечният каталог идва от публичния каталог на
  [InvLib](https://invlib.com/) — системата, с която работи библиотеката.
- Без WebView за съдържанието, без реклами, без проследяване.

> Проучването на източниците (сайтът, `katalog.json`, InvLib) е в
> **[ANALYSIS.md](ANALYSIS.md)**, а договорът с моста за читателските данни — в
> **[docs/API.md](docs/API.md)**.

## Съдържание

- [Екрани](#екрани)
- [Архитектура](#архитектура)
- [Технологии](#технологии)
- [Настройка и build](#настройка-и-build)
- [Конфигурация](#конфигурация)
- [API](#api)
- [Разработка и Development Mode](#разработка-и-development-mode)
- [Тестове](#тестове)
- [Release и Google Play](#release-и-google-play)
- [Автоматично обновяване](#автоматично-обновяване)
- [Фонова работа](#фонова-работа)
- [Поверителност](#поверителност)
- [Известни ограничения](#известни-ограничения)
- [Оставащи зависимости](#оставащи-зависимости)

## Екрани

| Раздел | Какво има |
|---|---|
| **Начало** | логото и името, празникът на деня, въртяща се лента с последните новини (parallax), 8 бързи действия, „Читалището в числа“ (анимирани броячи), предстоящи събития, нови постъпления и витрини, още новини |
| **Каталог** | instant search (заглавие, автор, ISBN, ключова дума, инв. номер), предложения, филтри (само налични, вид, раздел по УДК, отдел, език, година), сортиране, страница на книга (корица, данни, сигнатура, статус с дата на данните, други екземпляри, от същия автор, споделяне, „Заяви“ — само ако сървърът го поддържа) |
| **Моето** | гост / вход (карта + ПИН), читателска карта (Code 39 + QR, цял екран с максимална яркост), моите книги (срок с 🟢/🟠/🔴 индикатор от реалните дати, подновяване, ако мостът го позволява), „Какво съм чел“ с графики, членство, профил, известия, изход |
| **Новини** | търсене, категории, запазени (офлайн), пълна статия с hero снимка, галерия, споделяне |
| **Още** | събития (годишният календар на сайта: списък по категории + месечен календар, добавяне в календара, напомняне), дейности (страниците на сайта), за читалището, галерия (албуми от сайта, преглед със zoom/swipe), документи и исторически публикации (PDF), контакти (адрес, лица за контакт, работно време, имейл, карта, сайт), настройки, поверителност, за приложението |
| **Съобщения** | съобщенията от читалището (звънче с брой непрочетени на началния екран, известия; „само за членове“ се виждат след въвеждане на карта/вход). Изпращат се от админ панела на сайта — раздел „Съобщения до приложението“ ([docs/API.md](docs/API.md) 1.4a) |
| Глобално търсене | книги, автори, новини, събития, страници; скорошни търсения; филтри |
| Скенер | баркод на книга (ISBN), етикет с инвентарен номер или QR от каталога → книгата; CameraX + ZXing, всичко на устройството |
| Уиджет и преки пътища | уиджет (книги за връщане и сроковете им, следващо събитие — без заглавия на книги); преки пътища: каталог, карта, събития, скенер |
| Onboarding | 5 кратки екрана → вход или „Продължи без вход“ |

Екранни снимки за Play: `store/screenshots/` (от `PlayScreenshotTest` в CI с примерни
данни — виж [store/screenshots/README.md](store/screenshots/README.md)).
Графики за Play: `store/graphics/`.

## Архитектура

Подробно: **[docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)**.

```
app/             Android: Compose UI (MVVM), навигация, Room/DataStore/файлов кеш,
                 Android Keystore, WorkManager, уиджет, самообновяване, DI (AppContainer)
shared/          чист Kotlin: домейн модели, интерфейси на услугите, репозитории,
                 парсери за chyavorec.org (Vercel) и katalog.json (InvLib), клиент за
                 моста към InvLib — компилира се и се тества без Android SDK
baselineprofile/ Macrobenchmark генератор на Baseline/Startup профила
                 (резултатът: app/src/main/generated/baselineProfiles/)
tools/           генератори: текстове (generate_strings.py), икони, шрифтове, отчет за зависимостите
docs/            API.md, ARCHITECTURE.md, RELEASE.md (лоша версия), ROADMAP.md (план за развитие)
store/           Google Play метаданни, графики, политика за поверителност
.github/         CI, издания, Dependabot, Baseline profile
```

Интерфейси: `CatalogService`, `NewsService`, `EventsService`,
`SiteContentService`, `AuthenticationService`, `ReaderService`,
`MembershipService`. Репозитории: `CatalogRepository`, `LibraryRepository`,
`NewsRepository`, `EventsRepository`, `SiteRepository`, `AuthRepository`,
`ReaderDataRepository`, `ProfileRepository`, `MembershipRepository`,
`ReaderMessagesRepository`, `SelfCardRepository`.

## Технологии

Kotlin 2.4 · Jetpack Compose (BOM 2026.09) · Material 3 · Navigation Compose ·
Coroutines/Flow · kotlinx.serialization · OkHttp 5 · Jsoup · Room · DataStore ·
WorkManager · Glance (уиджет) · Coil 3 · CameraX · ZXing (баркодовете на картата и
разчитане в скенера) · Android Keystore · Baseline Profiles ·
AGP 9.4 (вграден Kotlin) · Gradle 9.8 · Java 21 (build с JDK 25) · minSdk 26 (Android 8.0) · target/compileSdk 37 (Android 17).

**Автоматично: обновяване → сливане → build → издание**

1. Dependabot (`.github/dependabot.yml`) всяка седмица отваря PR към `main` с
   новите версии на Kotlin, AGP, AndroidX, библиотеките, Gradle wrapper и
   GitHub Actions (групирани: kotlin / android / libraries / actions).
2. CI (`android.yml`) пуска тестовете, lint и release build.
3. Ако е зелен, `dependabot-automerge.yml` слива PR-а в `main` (само patch/minor;
   major обновяванията остават отворени за ръчен преглед).
4. `auto-release.yml` издава нова версия (patch +1, напр. v1.1.0 → v1.1.1) —
   също и след всеки push в `main`, който променя приложението (`app/`, `shared/`,
   `gradle/`, Gradle скриптовете и `gradle.properties`, `compose-stability.conf`;
   не документация/CI). Release-ът съдържа APK, AAB и `update.json`, а
   инсталираните приложения се обновяват сами. Изданието е **чернова** (публикува
   се ръчно) само ако след последното издание всички commit-и са от Dependabot или
   от файловете на приложението са пипнати само `gradle/libs.versions.toml` и Gradle
   wrapper-ът. Промяна напр. само в `app/build.gradle.kts` (адреси, `minSdk`, R8) е
   истинско издание.

Всяка неделя `baseline-profile.yml` генерира наново Baseline/Startup профила на
емулатор и го предлага в клона `baseline-profile/update` (слива се ръчно).

Ръчно: Actions → Release (конкретна версия, напр. `1.2.0`) или tag `v1.2.0`.
При ръчно пускане (Release, Auto release, Update control) в „Run workflow“ избери
**Use workflow from: `main`** (клонът по подразбиране; `main` е защитен — нужен е зелен
„checks“). От друг клон
Auto release и Update control спират с грешка, а Release подписва с истинския ключ само от `main`.
Лоша версия — спиране на обновяванията (`update-control.yml`) и връщане назад:
**[docs/RELEASE.md](docs/RELEASE.md)**.
Текущото състояние спрямо последните версии: Actions → „Dependency report“.

Шрифтове: Cormorant Garamond и Raleway (SIL Open Font License, с кирилица) —
същите като на сайта. Лицензите: [docs/fonts/](docs/fonts/).

## Настройка и build

Изисквания: **JDK 21+** (CI ползва JDK 25), **Android SDK** с platform 37 (Android Studio
Narwhal/по-нова го инсталира автоматично).

```bash
git clone https://github.com/plam4o4o-source/ch-app.git
cd ch-app
cp .env.example .env          # по желание — стойностите по подразбиране работят
./gradlew assembleProdDebug   # APK: app/build/outputs/apk/prod/debug/
```

Или отворете папката в Android Studio и изберете варианта `prodDebug`
(истински данни) или `devDebug` (с демо читателски данни).

## Конфигурация

Всички стойности са в [`.env.example`](.env.example). Приоритет:
променлива на средата → `-PИМЕ=…` → `.env` → стойност по подразбиране.
`.env`, ключовете за подписване и `local.properties` са в `.gitignore`.

| Ключ | По подразбиране | Описание |
|---|---|---|
| `API_BASE_URL` | `https://chyavorec.org` | сайтът на читалището |
| `CATALOG_URLS` | raw.githubusercontent … `\|` cdn.jsdelivr … | основен и резервен адрес на `katalog.json` |
| `INFLIB_API_URL` | `https://chyavorec.org/api/invlib/yavorec` | мостът за онлайн достъп на читатели (само `https://`); празно = функцията е „недостъпна“ |
| `UPDATE_MANIFEST_URL` | `…/releases/latest/download/update.json` | откъде APK-то извън Google Play проверява за нова версия; празно = без самообновяване |
| `APP_ENV` | `development` | само за dev; prod е винаги `production` |
| `USE_MOCK_DATA` | `true` | само за dev flavor — демо читателски данни |
| `VERSION_CODE` | `1` | версия за Google Play |
| `SIGNING_*` | *(празно)* | release подписване (виж по-долу) |

В кода няма API ключове и тайни.

## API

**[docs/API.md](docs/API.md)** описва:

1. chyavorec.org (Vercel) — `/data/news.json`, `/rss.xml`, `/javora/index.json`, `/data/files.json`, `/api/calendar`, страници, контакти;
2. публичния `katalog.json` на InvLib (реален формат);
3. **предложения** договор за онлайн API на InvLib (вход, профил, заемания,
   членство, заявки, подновяване) — клиентът е готов в приложението и се
   включва само със задаване на `INFLIB_API_URL`.

## Разработка и Development Mode

| Вариант | ID | Читателски данни |
|---|---|---|
| `devDebug` | `org.chyavorec.app.dev` | `USE_MOCK_DATA=true` → **ДЕМО** данни (вход: `DEMO-0001` / `demo`), с жълта лента „ДЕМО ДАННИ“ на всеки такъв екран |
| `prodDebug`, `prodRelease` | `org.chyavorec.app` | **никога** демо данни — демо кодът е само в `app/src/dev` и не се компилира в prod |

Новините, събитията, страниците и каталогът са **винаги реални** — и в dev.

Ядрото може да се разработва и без Android SDK:

```bash
cd shared && ../gradlew test
```

## Тестове

```bash
cd shared && ../gradlew test && cd ..                 # ядро: парсери, търсене, репозитории, API (MockWebServer)
./gradlew testDevDebugUnitTest testProdDebugUnitTest   # ViewModel, сигурно съхранение, Compose UI (Robolectric)
./gradlew lintDevDebug lintProdRelease
./gradlew assembleDevDebug assembleProdDebug
./gradlew bundleProdPlay                               # AAB за Google Play (без самообновяване)
./gradlew assembleProdRelease                          # APK за директно инсталиране (със самообновяване)
```

GitHub Actions (`.github/workflows/android.yml`) пуска всичко това при push в `main`,
`claude/**` и `wave*/**`, при pull request (без повторение за commit, проверен вече при push)
и ръчно, и качва APK/AAB като artifacts. Първо `checks` — само с права за четене (тестове на
`shared`, тестовете на приложението за prod и `MainFlowsUiTest` за dev — заради демо входа,
проверка, че Room схемите в `app/schemas/` са commit-нати, lint, debug APK-та, компилиране на
генератора на профила); след като мине — `release` (подписаните AAB/APK и проверка на
манифеста за Play) и `publish-screenshots`: Robolectric снимките на екраните (`ScreenshotTest`
и др.) от artifact-а на `checks` се публикуват при push в клона `screenshots`. Отчетът на
Compose компилатора е само при ръчно пускане.

## Release и Google Play

1. **Upload ключ** (веднъж; пазете го извън Git):
   ```bash
   keytool -genkeypair -v -keystore upload.jks -alias upload -keyalg RSA -keysize 4096 -validity 10000
   ```
2. **Локален build:** в `.env` (или като променливи на средата):
   ```
   SIGNING_STORE_FILE=/път/до/upload.jks
   SIGNING_STORE_PASSWORD=…
   SIGNING_KEY_ALIAS=upload
   SIGNING_KEY_PASSWORD=…
   VERSION_CODE=1
   ```
   ```bash
   ./gradlew bundleProdPlay
   # → app/build/outputs/bundle/prodPlay/app-prod-play.aab
   ```
3. **Build в GitHub Actions:** добавете secrets `UPLOAD_KEYSTORE_BASE64`
   (`base64 -w0 upload.jks`), `UPLOAD_STORE_PASSWORD`, `UPLOAD_KEY_ALIAS`,
   `UPLOAD_KEY_PASSWORD`. Без тях CI подписва с временен тестов ключ
   (artifact `release-ci-test-only`) — **такъв AAB не се качва в Play**.
4. **Play Console:** включете Play App Signing, качете AAB, попълнете данните
   от [store/play-listing.md](store/play-listing.md) и формуляра Data safety;
   публикувайте [store/privacy-policy.md](store/privacy-policy.md) като
   страница в chyavorec.org и въведете адреса ѝ.
5. Изданията от `release.yml` задават версиите сами: `versionName` = въведената
   версия, `VERSION_CODE` = минути от 01.01.2024 (расте монотонно). При локален
   build увеличете `VERSION_CODE` ръчно.

## Автоматично обновяване

| Инсталация | Как се обновява |
|---|---|
| APK от [GitHub Releases](https://github.com/plam4o4o-source/ch-app/releases) (`prodRelease`) | **самото приложение** — виж по-долу |
| Google Play (`prodPlay` AAB) | от Google Play (правилата на Play забраняват самообновяване, затова този build е без него и без разрешението `REQUEST_INSTALL_PACKAGES`) |
| `devDebug` / `prodDebug` | изключено |

Как работи (`app/.../update/`, `shared/.../data/update/`):

1. При отваряне (най-много веднъж на 12 часа; ръчната проверка от настройките —
   винаги) и веднъж на ~24 часа във фона (общата задача на WorkManager) приложението чете
   `update.json` от последното GitHub Release: `versionCode`, `versionName`,
   `apkUrl`, `sha256`, `size`, `minSdk`, `notes` (и `paused` — спряна версия не
   се предлага, виж [docs/RELEASE.md](docs/RELEASE.md)).
2. Ако `versionCode` е по-висок, по Wi-Fi новата версия се изтегля веднага
   (по мобилни данни — след „Изтегли и инсталирай“).
3. Файлът се проверява: SHA-256 от манифеста, същият пакет и версия, **същият
   ключ за подписване** като инсталираното приложение (или законна смяна на ключа).
   Иначе — и ако подписите не могат да се разчетат — се изтрива.
4. Инсталиране през `PackageInstaller`:
   - когато приложението е отворено — диалог „Нова версия X“ → „Инсталирай“;
     първия път Android иска разрешение „Инсталиране на неизвестни приложения“;
   - на Android 12+, след като приложението веднъж се е обновило само, следващите
     версии се инсталират **без въпрос**, докато приложението не се използва;
   - иначе — известие „Версия X е готова за инсталиране“.
5. Настройки → Обновления: вкл./изкл. и „Провери за нова версия“.
   „По-късно“ отлага същата версия с 24 часа.

**Издаване на нова версия:** Actions → Release → Run workflow от `main` (версия, напр.
`1.2.0`, и по желание „Какво ново“). Workflow-ът качва APK, AAB и `update.json`;
инсталираните приложения го откриват сами.

> ⚠ **Необходим е постоянен ключ за подписване** (secrets `UPLOAD_KEYSTORE_BASE64`,
> `UPLOAD_STORE_PASSWORD`, `UPLOAD_KEY_ALIAS`, `UPLOAD_KEY_PASSWORD` — виж
> „Release и Google Play“). Android приема обновление само ако е подписано със
> същия ключ. Без тези secrets всеки release би се подписал с нов временен ключ и
> затова workflow-ът тогава **не публикува** `update.json`. Ключът е зададен и
> изданията (v1.2.x) носят `update.json`; само най-ранната v1.0.0 беше с временен
> ключ и се преинсталира еднократно ръчно.

## Графика и лога

| Ресурс | Файл |
|---|---|
| Логото на читалището (заглавна лента, Play икона) | `app/src/main/res/drawable-nodpi/logo_chitalishte.webp` (+ `logo_chitalishte_small.webp`, 256 px — за емблеми до 64dp) |
| Иконата в лаунчера и splash — опростеният медальон (пръстен + книга), векторно | `drawable/ic_launcher_foreground.xml`, `ic_launcher_background.xml`, `ic_launcher_monochrome.xml`, `splash_icon.xml` (+ анимирана в `drawable-v31`) |
| Логото на електронния каталог | `drawable-nodpi/logo_catalog.webp` |
| Логото на InvLib | `drawable-nodpi/logo_invlib.webp` |
| Логото на създателя | `drawable-nodpi/logo_creator.webp` |
| Икона и feature graphic за Google Play | `store/graphics/` |
| Иконата за Google Play — генерира се от логото | `python3 tools/generate_icons.py` |

Текстовете на двата езика се генерират от един файл: `python3 tools/generate_strings.py`.

Шрифтовете в `res/font` са подмножества (латиница, кирилица, пунктуация, € №) — при нов шрифт или нови знаци в текстовете: `python3 tools/subset_fonts.py` (проверява и покритието на strings.xml). Цифрите са изравнени (lining): `lnum` е замразен в самия шрифт, за да не се чете „1“ като „I“; подмножеството на Raleway е с вътрешно име „Yavorec Sans“ (Reserved Font Name по OFL). Наново от пълните шрифтове: `--source <папка>` (подробности в скрипта).

## Фонова работа

Всичко е през WorkManager, само с интернет и достатъчно батерия:

| Задача | Кога | Какво |
|---|---|---|
| `PeriodicSyncWorker` | на ~6 ч. | лични съобщения (при вход); нова новина и заемания (просрочие) — на ~12 ч., ако са включени; събития — само за уиджета; проверка за нова версия — на ~24 ч. (само APK извън Play) |
| `MessagePollWorker` | буди се на ~1 ч., пита на 1–6 ч. | общите съобщения (`/data/app-messages.json`, условна заявка); интервалът се удвоява, докато файлът не се променя; спира, ако приложението не е отваряно 14 дни |
| `LoanReminderWorker`, `EventReminderWorker` | еднократно, в часа на напомнянето | локални напомняния за срок на книга и за събитие — без мрежа |

Всяка стъпка проверява първо настройките и разрешението за известия и не пита мрежата
излишно. Правилата (`MessagePollPolicy`, `LoanReminderPlan` …) са в `shared/.../domain/model/`.

## Поверителност

- Без реклами, analytics, crash reporting и Firebase.
- Локален дневник на грешките без лични данни — само на устройството; напуска го,
  само ако читателят сам го сподели (Настройки → Информация → „Диагностика“).
- Сесия, ръчно въведена карта и читателски кеш — шифровани с Android Keystore.
- Паролата никога не се записва; „не ме помни“ пази сесията само в паметта.
- Няма архивиране в облака; изходът изтрива личните данни от устройството.
- Пълен текст: [store/privacy-policy.md](store/privacy-policy.md) (и в приложението).

## Известни ограничения

- **Читателските данни са само през моста** на chyavorec.org
  (`INFLIB_API_URL`): InvLib изпраща на няколко минути снимка само на
  съгласилите се читатели (данни „към …“). Заявяване, подновяване, наличност по
  екземпляр и забравен ПИН остават изключени, докато мостът не ги обяви в
  `/v1/capabilities`. Без мост (празен адрес) тези екрани обясняват защо няма данни.
- Наличността в каталога е двустепенна („налична“ / „не е на рафта“) и към
  датата на последното публикуване от InvLib.
- ISBN не е в публичния каталог → търсенето по ISBN работи, щом InvLib добави ключ `i`.
- Календарът на събитията на сайта е годишен (ден + месец, без час и място) —
  напомнянето се задава за деня на събитието.
- Push известия от сървър няма (няма FCM); известията идват от фоновите
  задачи (виж „Фонова работа“) и затова закъсняват до няколко часа.
- Съдържанието на отделните страници се извлича от HTML (`#hp .hp-inner`) —
  при голяма промяна в разметката на сайта вижте „Проверка“ в ANALYSIS.md.
- Телефон за контакт не е публикуван на сайта, затова бутон „Обади се“ се
  появява едва когато се добави телефон в страницата „Контакти“.

## Оставащи зависимости

План за следващата стъпка (заеманията на читателя): **[docs/ROADMAP.md](docs/ROADMAP.md)**.

| Зависимост | От кого | Какво отключва |
|---|---|---|
| Включен „Онлайн достъп за читатели“ в InvLib на библиотеката (код за активация, ключ за качване) и съгласие + ПИН на читателите | читалището | вход, профил, моите книги, членство, известия за срокове (мостът е готов — [docs/API.md](docs/API.md) 3) |
| Заявяване и подновяване в моста (опашка към InvLib) | моста / InvLib | „Заяви“, „Поднови“ |
| Ключ `i` (ISBN) в `katalog.json` | InvLib (`publicBookFields`) | търсене по ISBN |
| Статус „заета“ vs „недостъпна“ в `katalog.json` или `/v1/books/{inv}/availability` | InvLib | 3-степенна наличност |
| Страница „Политика за поверителност“ в chyavorec.org | читалището | URL за Google Play |
| Upload ключ и акаунт в Google Play Console | читалището | публикуване |
| Час и място в календара на събитията (index.json) | сайта | часове в календара и напомняне 2 ч. преди началото |
| Сървър за push (FCM) — по избор | читалището / InvLib | мигновени известия |
