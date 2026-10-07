# Политика за поверителност — приложение „Читалище Яворец“

## Кой обработва данните

Приложението „Читалище Яворец“ е официалното мобилно приложение на Народно читалище „Васил Левски – 1922“, с. Яворец, общ. Габрово (наричано по-долу „читалището“). Връзка: chitalishte_yavorec@abv.bg.

## Принцип

Приложението събира възможно най-малко данни. То няма реклами, няма анализ на поведението (analytics), няма услуги за проследяване и не изпраща отчети за сривове към трети страни.

## Какви данни се обработват

Публично съдържание: новините, събитията, снимките и страниците се зареждат от сайта chyavorec.org, а библиотечният каталог — от публичния файл katalog.json на системата InvLib, хостван в GitHub (raw.githubusercontent.com и cdn.jsdelivr.net). При всяка такава заявка съответният сървър вижда IP адреса на устройството, както при посещение на обикновен уебсайт. Каталогът не съдържа лични данни на читатели.

**Съобщения от читалището:** приложението проверява на около час публичния файл със съобщения на chyavorec.org. Съобщенията „само за членове“ се показват, ако си въвел читателска карта или си влязъл — тази проверка е на устройството и нищо не се изпраща към сайта. Прочетените съобщения се помнят само на устройството.

**Обновления:** версията, инсталирана извън Google Play, проверява за нова версия във файла update.json в GitHub (github.com) и изтегля новата версия оттам. Изпраща се само обикновена заявка (IP адресът е видим за GitHub); не се изпращат лични данни. Автоматичното обновяване се изключва от Настройки → Обновления. Версията от Google Play се обновява от Google Play.

Данни само на устройството: настройки (тема, език, известия), запазени новини, напомняния за събития, история на търсенията и кеширано публично съдържание за офлайн работа. Те не напускат устройството и не се архивират в облака.

Читателска карта: ако въведеш номера на своята карта, той (и името, ако го въведеш) се пази шифрован с ключ от Android Keystore само на това устройство.

Вход (само ако си дал съгласие в библиотеката и имаш издаден ПИН): номерът на читателската карта и ПИН-ът се изпращат само по HTTPS към сървъра на читалището (chyavorec.org) и ПИН-ът никога не се записва. При вход се изпраща и моделът на телефона (напр. „Pixel 8“), за да се разпознава сесията. Пази се само шифрован токен за сесията. Името ти, заетите книги, сроковете и членството идват от този сървър, който получава от библиотечната програма само данните на съгласилите се читатели (без ЕГН, адрес и телефон), и се кешират шифровано на устройството. Съгласието се оттегля в библиотеката — тогава данните ти се изтриват от сървъра при следващото обновяване.

Защита на входа: след няколко неуспешни опита за вход входът временно се блокира (на устройството и на сървъра), за да не може ПИН-ът да бъде отгатнат.

Уиджет на началния екран: ако го добавиш, той показва само броя книги за връщане и най-близкия срок — без заглавия и без име.

Скенер на баркодове: камерата се включва само когато отвориш скенера и разрешиш достъп; кадрите се разчитат на телефона и не се записват и не се изпращат.

История на четенето: за читатели, дали съгласие в библиотеката, списъкът с върнати книги идва от сървъра на читалището и се пази шифрован на телефона. „Подобни книги“ се подбират само на телефона.

Удължаване на срока: заявката се изпраща до сървъра на читалището и се изпълнява от библиотечната програма при следващото ѝ обновяване.

## Известия

Известията се подготвят на устройството (периодична проверка за нови публикации и срокове). Не се използват сървъри за push известия.

## Твоите права

Можеш по всяко време да изтриеш данните на устройството: „Изход“ изтрива сесията и читателските данни; „Премахни“ изтрива въведената карта; „Изчисти кеша“ изтрива публичното съдържание; деинсталирането премахва всичко. Изтриване на профила за приложението и данните ти от сървъра можеш да поискаш на https://chyavorec.org/app-delete, в читалището или по имейл. За данните, които библиотеката поддържа за теб като читател (по Наредба № 3 и ОРЗД), можеш да поискаш достъп, корекция или изтриване в читалището или по имейл. Имаш право на жалба до Комисията за защита на личните данни (www.cpdp.bg).

## Деца

Приложението не е насочено към деца под 13 години. То може да се използва без вход и без никакви лични данни.

## Промени

При промяна на тази политика новата версия се публикува в приложението и в сайта на читалището.

---

# Privacy policy — “Chitalishte Yavorets” app

## Who processes the data

“Chitalishte Yavorets” is the official mobile app of the “Vasil Levski – 1922” Community Centre (chitalishte), Yavorets village, Gabrovo municipality, Bulgaria (“the centre”). Contact: chitalishte_yavorec@abv.bg.

## Principle

The app collects as little data as possible. It has no ads, no behavioural analytics, no tracking services and does not send crash reports to third parties.

## What data is processed

Public content: news, events, photos and pages are loaded from chyavorec.org, and the library catalogue from the public katalog.json file of the InvLib system hosted on GitHub (raw.githubusercontent.com and cdn.jsdelivr.net). Each such server sees the device’s IP address, as with any website visit. The catalogue contains no reader data.

**Messages from the centre:** about once an hour the app checks the public messages file on chyavorec.org. Members-only messages are shown if you have entered your library card or signed in — this check happens on the device and nothing is sent to the website. Read messages are remembered only on the device.

**Updates:** the version installed outside Google Play checks for a new version in the update.json file on GitHub (github.com) and downloads it from there. Only a plain request is sent (GitHub sees the IP address); no personal data is sent. Automatic updates can be turned off in Settings → Updates. The Google Play version is updated by Google Play.

Data kept only on the device: settings (theme, language, notifications), saved news, event reminders, search history and cached public content for offline use. It never leaves the device and is not backed up to the cloud.

Library card: if you enter your card number, it (and your name, if entered) is stored encrypted with an Android Keystore key on this device only.

Sign-in (only if you have given consent at the library and have been issued a PIN): your library card number and PIN are sent only over HTTPS to the centre’s server (chyavorec.org), and the PIN is never stored. The phone’s model name (e.g. “Pixel 8”) is also sent at sign-in so that the session can be recognised. Only an encrypted session token is kept. Your name, borrowed items, due dates and membership come from this server, which receives from the library software only the data of readers who have consented (no personal ID number (EGN), address or phone number), and are cached encrypted on the device. Consent is withdrawn at the library — your data is then deleted from the server at the next sync.

Sign-in protection: after several failed sign-in attempts, sign-in is temporarily blocked (on the device and on the server) so that the PIN cannot be guessed.

Home-screen widget: if you add it, it shows only the number of books due and the nearest due date — no titles and no name.

Barcode scanner: the camera turns on only when you open the scanner and allow access; frames are read on the phone and are never stored or sent.

Reading history: for readers who consented at the library, the list of returned books comes from the centre’s server and is kept encrypted on the phone. “Similar books” are picked on the phone only.

Renewals: a renewal request goes to the centre’s server and is carried out by the library program at its next update.

## Notifications

Notifications are prepared on the device (periodic checks for new posts and due dates). No push notification servers are used.

## Your rights

You can delete the data on the device at any time: “Sign out” deletes the session and reader data; “Remove” deletes the entered card; “Clear cache” deletes public content; uninstalling removes everything. You can request deletion of your app account and your data on the server at https://chyavorec.org/app-delete, at the centre or by email. For the data the library keeps about you as a reader (under Bulgarian Ordinance No. 3 and the GDPR), you can request access, correction or deletion at the centre or by email. You may lodge a complaint with the Bulgarian Commission for Personal Data Protection (www.cpdp.bg).

## Children

The app is not directed at children under 13. It can be used without signing in and without any personal data.

## Changes

If this policy changes, the new version is published in the app and on the centre’s website.
