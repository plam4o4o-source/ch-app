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

> Какво реално е налично и какво липсва (особено онлайн API на InvLib за
> читателски данни) е описано в **[ANALYSIS.md](ANALYSIS.md)**.

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
- [Поверителност](#поверителност)
- [Известни ограничения](#известни-ограничения)
- [Оставащи зависимости](#оставащи-зависимости)

## Екрани

| Раздел | Какво има |
|---|---|
| **Начало** | логото и името, празникът на деня, въртяща се лента с последните новини (parallax), 8 бързи действия, „Читалището в числа“ (анимирани броячи), предстоящи събития, нови постъпления и витрини, още новини |
| **Каталог** | instant search (заглавие, автор, ISBN, ключова дума, инв. номер), предложения, филтри (само налични, вид, раздел по УДК, отдел, език, година), сортиране, страница на книга (корица, данни, сигнатура, статус с дата на данните, други екземпляри, от същия автор, споделяне, „Заяви“ — само ако сървърът го поддържа) |
| **Моето** | гост / вход, читателска карта (Code 39 + QR, цял екран с максимална яркост), моите книги (срок с 🟢/🟠/🔴 индикатор от реалните дати), членство, профил, известия, изход |
| **Новини** | търсене, категории, запазени (офлайн), пълна статия с hero снимка, галерия, споделяне |
| **Още** | събития (годишният календар на сайта: списък по категории + месечен календар, добавяне в календара, напомняне), дейности (страниците на сайта), за читалището, галерия (албуми от сайта, преглед със zoom/swipe), документи и исторически публикации (PDF), контакти (адрес, лица за контакт, работно време, имейл, карта, сайт), настройки, поверителност, за приложението |
| **Съобщения** | съобщенията от читалището (звънче с брой непрочетени на началния екран, известия; „само за членове“ се виждат след въвеждане на карта/вход). Изпращат се от админ панела на сайта — раздел „Съобщения до приложението“ ([docs/API.md](docs/API.md) 1.4a) |
| Глобално търсене | книги, автори, новини, събития, страници; скорошни търсения; филтри |
| Onboarding | 5 кратки екрана → вход или „Продължи без вход“ |

Екранни снимки: `store/screenshots/` (правят се след първото пускане срещу
живия сайт — виж [store/play-listing.md](store/play-listing.md)).
Графики за Play: `store/graphics/`.

## Архитектура

Подробно: **[docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)**.

```
app/      Android: Compose UI (MVVM), навигация, Room/DataStore/файлов кеш,
          Android Keystore, WorkManager известия, DI (AppContainer)
shared/   чист Kotlin: домейн модели, интерфейси на услугите, репозитории,
          парсери за chyavorec.org (Vercel) и katalog.json (InvLib), клиент за
          бъдещия InvLib API — компилира се и се тества без Android SDK
docs/     API.md, ARCHITECTURE.md
store/    Google Play метаданни, графики, политика за поверителност
```

Интерфейси: `CatalogService`, `NewsService`, `EventsService`,
`SiteContentService`, `AuthenticationService`, `ReaderService`,
`MembershipService`. Репозитории: `CatalogRepository`, `LibraryRepository`,
`NewsRepository`, `EventsRepository`, `SiteRepository`, `AuthRepository`,
`MembershipRepository`, `ProfileRepository`.

## Технологии

Kotlin 2.2 · Jetpack Compose (BOM 2025.06) · Material 3 · Navigation Compose ·
Coroutines/Flow · kotlinx.serialization · OkHttp 4 · Jsoup · Room · DataStore ·
WorkManager · Coil · ZXing (само кодиране) · Android Keystore ·
AGP 8.11 · Gradle 8.14 · minSdk 26 (Android 8.0) · target/compileSdk 36.

Шрифтове: Cormorant Garamond и Raleway (SIL Open Font License, с кирилица) —
същите като на сайта.

## Настройка и build

Изисквания: **JDK 17+**, **Android SDK** с platform 36 (Android Studio
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
| `INFLIB_API_URL` | *(празно)* | онлайн API на InvLib (само `https://`); празно = функцията е „недостъпна“ |
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

GitHub Actions (`.github/workflows/android.yml`) пуска всичко това при всеки
push и качва APK/AAB като artifacts.

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
5. При всяка нова версия увеличете `VERSION_CODE` и `versionName`
   (`app/build.gradle.kts`).

## Автоматично обновяване

| Инсталация | Как се обновява |
|---|---|
| APK от [GitHub Releases](https://github.com/plam4o4o-source/ch-app/releases) (`prodRelease`) | **самото приложение** — виж по-долу |
| Google Play (`prodPlay` AAB) | от Google Play (правилата на Play забраняват самообновяване, затова този build е без него и без разрешението `REQUEST_INSTALL_PACKAGES`) |
| `devDebug` / `prodDebug` | изключено |

Как работи (`app/.../update/`, `shared/.../data/update/`):

1. При всяко отваряне и на ~12 часа във фона (WorkManager) приложението чете
   `update.json` от последното GitHub Release: `versionCode`, `versionName`,
   `apkUrl`, `sha256`, `size`, `minSdk`, `notes`.
2. Ако `versionCode` е по-висок, по Wi-Fi новата версия се изтегля веднага
   (по мобилни данни — след „Изтегли и инсталирай“).
3. Файлът се проверява: SHA-256 от манифеста, същият пакет и версия, **същият
   ключ за подписване** като инсталираното приложение. Иначе се изтрива.
4. Инсталиране през `PackageInstaller`:
   - когато приложението е отворено — диалог „Нова версия X“ → „Инсталирай“;
     първия път Android иска разрешение „Инсталиране на неизвестни приложения“;
   - на Android 12+, след като приложението веднъж се е обновило само, следващите
     версии се инсталират **без въпрос**, докато приложението не се използва;
   - иначе — известие „Версия X е готова за инсталиране“.
5. Настройки → Обновления: вкл./изкл. и „Провери за нова версия“.
   „По-късно“ отлага същата версия с 24 часа.

**Издаване на нова версия:** Actions → Release → Run workflow (версия, напр.
`1.2.0`, и по желание „Какво ново“). Workflow-ът качва APK, AAB и `update.json`;
инсталираните приложения го откриват сами.

> ⚠ **Необходим е постоянен ключ за подписване** (secrets `UPLOAD_KEYSTORE_BASE64`,
> `UPLOAD_STORE_PASSWORD`, `UPLOAD_KEY_ALIAS`, `UPLOAD_KEY_PASSWORD` — виж
> „Release и Google Play“). Android приема обновление само ако е подписано със
> същия ключ. Без тези secrets всеки release се подписва с нов временен ключ и
> затова workflow-ът **не публикува** `update.json`. Версиите до момента (v1.0.0)
> са с временен ключ — те трябва да се деинсталират и инсталират еднократно
> ръчно; оттам нататък обновяването е автоматично.

## Графика и лога

| Ресурс | Файл |
|---|---|
| Логото на читалището (икона, splash, заглавна лента) | `app/src/main/res/drawable-nodpi/logo_chitalishte.webp`, `mipmap-*/ic_launcher_*.png` |
| Логото на електронния каталог | `drawable-nodpi/logo_catalog.png` |
| Логото на InvLib | `drawable-nodpi/logo_invlib.png` |
| Логото на създателя | `drawable-nodpi/logo_creator.webp` |
| Икона и feature graphic за Google Play | `store/graphics/` |

Текстовете на двата езика се генерират от един файл: `python3 tools/generate_strings.py`.

## Поверителност

- Без реклами, analytics, crash reporting и Firebase.
- Сесия, ръчно въведена карта и читателски кеш — шифровани с Android Keystore.
- Паролата никога не се записва; „не ме помни“ пази сесията само в паметта.
- Няма архивиране в облака; изходът изтрива личните данни от устройството.
- Пълен текст: [store/privacy-policy.md](store/privacy-policy.md) (и в приложението).

## Известни ограничения

- **InvLib няма онлайн API** → в production вход, профил, „Моите книги“,
  членство, заявяване и известия за срокове показват ясно обяснение вместо
  данни. Включват се автоматично с `INFLIB_API_URL`.
- Наличността в каталога е двустепенна („налична“ / „не е на рафта“) и към
  датата на последното публикуване от InvLib.
- ISBN не е в публичния каталог → търсенето по ISBN работи, щом InvLib добави ключ `i`.
- Календарът на събитията на сайта е годишен (ден + месец, без час и място) —
  напомнянето се задава за деня на събитието.
- Push известия от сървър няма (няма backend); известията са локални
  (WorkManager, на ~12 часа).
- Съдържанието на отделните страници се извлича от HTML (`#hp .hp-inner`) —
  при голяма промяна в разметката на сайта вижте „Проверка“ в ANALYSIS.md.
- Телефон за контакт не е публикуван на сайта, затова бутон „Обади се“ се
  появява едва когато се добави телефон в страницата „Контакти“.

## Оставащи зависимости

| Зависимост | От кого | Какво отключва |
|---|---|---|
| Онлайн API на InvLib по [docs/API.md](docs/API.md) | разработчика на InvLib | вход, профил, карта от системата, моите книги, членство, известия за срокове, заявяване, подновяване |
| Ключ `i` (ISBN) в `katalog.json` | InvLib (`publicBookFields`) | търсене по ISBN |
| Статус „заета“ vs „недостъпна“ в `katalog.json` или `/v1/books/{inv}/availability` | InvLib | 3-степенна наличност |
| Страница „Политика за поверителност“ в chyavorec.org | читалището | URL за Google Play |
| Upload ключ и акаунт в Google Play Console | читалището | публикуване |
| Час и място в календара на събитията (index.json) | сайта | часове в календара и напомняне 2 ч. преди началото |
| Сървър за push (FCM) — по избор | читалището / InvLib | мигновени известия |
