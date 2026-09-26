# API документация

Приложението работи с три източника. Първите два **съществуват** и се
използват в production. Третият е **предложен договор** — такъв сървър още
няма; това е спецификацията, която трябва да реализира InvLib (или мост към
базата му), за да се включат читателските функции.

| # | Източник | Статус | Код |
|---|---|---|---|
| 1 | chyavorec.org (uCoz): RSS + страници | ✅ съществува | `shared/.../data/site/` |
| 2 | Публичен каталог на InvLib (`katalog.json` в GitHub) | ✅ съществува | `shared/.../data/catalog/` |
| 3 | Онлайн API на InvLib за читатели | ❌ **не съществува — предложение** | `shared/.../data/invlib/RemoteInvLibClient.kt` |

---

## 1. chyavorec.org

Всички заявки: `GET`, HTTPS, `User-Agent: ChitalishteYavorec-Android/<версия>`.
Кодировката се определя от заглавката `Content-Type` или XML/HTML декларацията.

### 1.1 Новини — `GET /news/rss/`

RSS 2.0, генериран от модула „Новини на сайта“ на uCoz.

| RSS елемент | Поле в приложението |
|---|---|
| `item/title` | `NewsArticle.title` (празните се пропускат) |
| `item/link` | `url` (http → https) |
| `item/guid` | `id` (ако липсва — `link`) |
| `item/pubDate` (RFC 1123) | `publishedAtMillis` |
| `item/description` (HTML) | `summary` (текст, до 400 знака) + първата снимка |
| `item/content:encoded` | `contentBlocks` (ако има) |
| `item/category` | `category` |
| `item/author` / `dc:creator` | `author` |
| `enclosure[type^=image]`, `media:content` | `imageUrl` (приоритет) |

### 1.2 Пълна новина — `GET <link на новината>`

Основният контейнер се търси по ред: `.eMessage`, `.eText`,
`.ec-message-text`, `.nf-body`, `#mc`, `main`, `article`, `#content`.
Премахват се: скриптове, стилове, форми, навигация, `#hp`, скритите елементи,
панелите с детайли/коментари/споделяне на uCoz. Резултатът е списък от
`ContentBlock` (Heading, Paragraph с форматиране и връзки, BulletList, Quote,
Image, LinkButton за PDF/DOC файлове). Иконки, емотикони и изображения под
48 px се игнорират.

### 1.3 Галерия — `GET /photo/rss/`

RSS на модула „Фотоалбуми“. `category` = албум. Миниатюрата
`/_ph/<N>/2/<id>.jpg` → оригинал `/_ph/<N>/<id>.jpg` (при грешка се показва
миниатюрата).

### 1.4 Навигация — `GET /`

Връзките в началната страница към `/index/<име>/<N>-<N>`, `/news/`, `/photo/`,
`/load/`, `/publ/`, `/blog/` се класифицират по заглавие (`SiteLinkClassifier`):
ABOUT, HISTORY, LIBRARY, CATALOG, EVENTS, FOLKLORE, DANCE, CLUBS, PROJECTS,
DIGITAL_CLUB, CONTACTS, GALLERY, NEWS, DOCUMENTS, DONATIONS, PUBLICATIONS,
VILLAGE, PRIVACY, TERMS, OTHER. Системните страници на uCoz (`/index/0-N` —
вход, регистрация) се изключват.

### 1.5 Събития

Няма структуриран календар. Събитие се създава, когато:

1. новина е в категория, съдържаща „събити“, „афиш“, „покан“, „календар“,
   „предстоящ“; **или**
2. заглавието съдържа „събитийна“ дума (покана, концерт, празник, тържество,
   изложба, среща, събор, вечер, честване, премиера, фестивал, конкурс,
   работилница, спектакъл, четене, беседа) **и** в текста има изрична дата; **или**
3. блок от страницата „Събития“ съдържа заглавие (`h1–h5`/`strong`) и дата.

Разпознавани формати: „12 октомври 2026 г.“, „12 окт. 2026“, „12.10.2026“,
„18:00 ч.“, „18.30 часа“, „Място: …“, „Организатор: …“. Ако в новина от
категория „Събития“ няма дата, се показва датата на публикуване с изрична
бележка (`dateIsExplicit = false`).

### 1.6 Контакти — страницата „Контакти“

`tel:` и `mailto:` връзки, телефони (`0XXX…`, `+359…`) и имейли в текста;
адрес — редът с „Яворец“/„ул.“/„пл.“; работно време — редове с интервал от
часове и ден от седмицата.

---

## 2. Публичен каталог на InvLib

```
GET https://raw.githubusercontent.com/plam4o4o-source/yavorec-katalog/main/katalog.json   (основен)
GET https://cdn.jsdelivr.net/gh/plam4o4o-source/yavorec-katalog@main/katalog.json        (резервен)
```

Форматът е описан подробно в [ANALYSIS.md, раздел 2.2](../ANALYSIS.md).
Парсерът (`KatalogParser`) е толерантен: непознати ключове се пренебрегват;
`inv` и `av` се приемат като число, низ или булева стойност; записи без
инвентарен номер или заглавие се пропускат; `cv` се приема само ако е
http(s) адрес. Ключ `i` (ISBN) се чете, ако InvLib започне да го публикува.

Адресите се конфигурират с `CATALOG_URLS` (виж `.env.example`).

---

## 3. Онлайн API на InvLib — ПРЕДЛОЖЕН ДОГОВОР

> ⚠ **Този API не съществува.** Приложението има готов клиент
> (`RemoteInvLibClient`), който се включва само ако е зададен `INFLIB_API_URL`
> (само `https://`). Без него production build-ът показва честно съобщение,
> а dev build-ът може да ползва ясно маркирани ДЕМО данни.

### 3.1 Общи правила

- Само HTTPS (TLS 1.2+). Базов адрес, напр. `https://api.example.bg/invlib`.
- JSON (UTF-8), дати в ISO-8601 (`2026-10-03`).
- Удостоверяване: `Authorization: Bearer <accessToken>`.
- Грешки: HTTP код + тяло `{"error":"<код>","message":"<текст>"}`.
  Приложението разпознава: `401/403` → нужен е нов вход; `404`; `429` +
  `Retry-After` (секунди); `5xx`.
- Непознати полета се пренебрегват (сървърът може да добавя полета свободно).
- Сървърът **трябва** да ограничава опитите за вход (напр. 5 опита / 15 мин.
  на карта и IP) — приложението има и собствено ограничение, но то не е
  достатъчна защита.

### 3.2 `GET /v1/capabilities`

Без удостоверяване. Казва на приложението кои функции да покаже.

```json
{
  "apiVersion": 1,
  "login": true,
  "profile": true,
  "loans": true,
  "membership": true,
  "holds": false,
  "renew": false,
  "passwordReset": false,
  "accountDeletion": false,
  "push": false,
  "availability": true
}
```

Бутоните „Заяви книгата“, „Поднови“, „Забравена парола“ и „Искане за
изтриване на данните“ се показват **само** при `true`.

### 3.3 `POST /v1/auth/login`

```json
{ "cardNumber": "R-0042", "password": "…", "deviceName": "Pixel 8" }
```
Отговор `200`:
```json
{ "accessToken": "…", "refreshToken": "…", "expiresIn": 3600, "readerId": "123" }
```
`401` при грешни данни. Паролата/ПИН-ът се съхранява на сървъра само като
хеш (Argon2id/bcrypt). `cardNumber` = `readers.card_no` в InvLib.

### 3.4 `POST /v1/auth/refresh`

`{ "refreshToken": "…" }` → същият отговор като при вход. `401` → приложението
изтрива сесията и личните кеширани данни. Препоръка: ротиране на refresh
токена при всяко използване; срок 30–90 дни.

### 3.5 `POST /v1/auth/logout`

С Bearer токен; анулира refresh токена. Отговор `204`.

### 3.6 `POST /v1/auth/password-reset` (по избор)

`{ "cardNumber": "R-0042" }` → `202` винаги (без да издава дали картата
съществува). Изисква имейл на читателя в InvLib.

### 3.7 `GET /v1/me`

```json
{
  "readerId": "123",
  "cardNumber": "R-0042",
  "fullName": "Иван Иванов",
  "photoUrl": null,
  "category": "възрастен",
  "email": "ivan@example.bg",
  "registeredOn": "2021-03-15"
}
```
Източник в InvLib: `readers` (`name`, `card_no`, `category`, `email`,
`registered_at`). **Никога** не се връщат ЕГН, № на лична карта, адрес.

### 3.8 `GET /v1/me/loans`

```json
{
  "loans": [
    {
      "loanId": "9876",
      "inv": 156,
      "title": "Под игото",
      "author": "Иван Вазов",
      "coverUrl": null,
      "dateOut": "2026-09-12",
      "dateDue": "2026-10-03",
      "renewals": 0,
      "canRenew": true
    }
  ]
}
```
Източник: `loans` с `date_in IS NULL` за читателя + `books`. Индикаторът
(🟢 достатъчно време / 🟠 наближава — до 3 дни / 🔴 просрочена) се изчислява
в приложението от `dateDue` и днешната дата (`LoanDueCalculator`).

### 3.9 `GET /v1/me/membership`

```json
{
  "memberNumber": "R-0042",
  "holderName": "Иван Иванов",
  "since": "2021-03-15",
  "validUntil": "2026-12-31",
  "status": "active",
  "barcode": "R-0042"
}
```
`status`: `active` | `expired` | `suspended`. Приложението допълнително
отбелязва членството като изтекло, ако `validUntil` е минала.
Източник: `readers.registered_at`, `re_registered_at`, `status`,
`suspended_until`. `barcode` = съдържанието на Code 39 баркода от картата.

### 3.10 По избор

| Метод | Тяло / отговор | Източник в InvLib |
|---|---|---|
| `POST /v1/me/holds` | `{ "inv": 156 }` → `201` | `holds` |
| `POST /v1/me/loans/{loanId}/renew` | → `200` + обновеният `LoanDto` | `loans.renewals`, `settings.extensions_count`, `extension_days` |
| `GET /v1/books/{inv}/availability` | `{ "inv":156, "status":"available|on_loan|unavailable", "dueOn": "2026-10-03" }` | `books.status`, `inventory`, `loans` |
| `DELETE /v1/me` | → `202` (искане за изтриване по чл. 17 ОРЗД) | обработва се от библиотекаря |
| `POST /v1/me/devices` | `{ "fcmToken": "…" }` (бъдещи push известия) | нова таблица |

### 3.11 Архитектура на моста (препоръка)

InvLib е офлайн програма на Windows — не бива да се излага директно в
интернет. Препоръчителен вариант:

```
[InvLib (Windows, SQLite)] --(периодичен износ само на нужните колони, шифрован)-->
[Мост-сървър (HTTPS, напр. малък VPS/облачна функция)] <--(HTTPS, Bearer)-- [Приложението]
```

- Износът съдържа само: `readers(id, card_no, name, category, email,
  registered_at, re_registered_at, status, suspended_until)`, отворените
  `loans`, `holds`, хеш на ПИН за вход. Без ЕГН/ЛК/адреси.
- Заявките за действия (заявяване, подновяване) се записват в опашка, която
  InvLib изтегля и потвърждава.
- InvLib вече има модел за публикуване (git → GitHub за каталога); мостът
  може да ползва същия подход с частно хранилище или директен HTTPS upload.
