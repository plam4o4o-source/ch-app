# -*- coding: utf-8 -*-
# Генератор на values/strings.xml (bg) и values-en/strings.xml (en) — един източник за двата езика.
# Изпълнение от корена на проекта: python3 tools/generate_strings.py
# key: (bg, en)
S = {
"app_title": ("Читалище Яворец", "Chitalishte Yavorets"),
"app_subtitle": ("НЧ „Васил Левски – 1922“", "Community Centre “Vasil Levski – 1922”"),
"org_short": ("НЧ „Васил Левски – 1922“", "“Vasil Levski – 1922”"),
"org_place": ("с. Яворец", "Yavorets village"),
"org_library_name": ("Библиотека при НЧ „Васил Левски – 1922“, с. Яворец", "Library of the “Vasil Levski – 1922” Community Centre, Yavorets"),
"tab_home": ("Начало", "Home"),
"tab_catalog": ("Каталог", "Catalogue"),
"tab_my": ("Моето", "My"),
"tab_news": ("Новини", "News"),
"tab_more": ("Още", "More"),
"qa_catalog": ("Каталог", "Catalogue"),
"qa_card": ("Читателска карта", "Library card"),
"qa_events": ("Събития", "Events"),
"qa_about": ("За читалището", "About us"),
"qa_contacts": ("Контакти", "Contacts"),
"action_all": ("Всички", "All"),
"action_back": ("Назад", "Back"),
"action_cancel": ("Отказ", "Cancel"),
"action_catalog": ("Към каталога", "Open catalogue"),
"action_clear": ("Изчисти", "Clear"),
"action_clear_filters": ("Без филтри", "Reset filters"),
"action_close": ("Затвори", "Close"),
"action_edit": ("Промени", "Edit"),
"action_load_more": ("Покажи още", "Show more"),
"action_login": ("Вход", "Sign in"),
"action_logout": ("Изход", "Sign out"),
"action_ok": ("Добре", "OK"),
"action_open_site": ("Отвори в сайта", "Open on the website"),
"action_remove": ("Премахни", "Remove"),
"action_retry": ("Опитай отново", "Try again"),
"action_save": ("Запази", "Save"),
"action_unsave": ("Премахни от запазените", "Remove from saved"),
"action_search": ("Търсене", "Search"),
"action_send": ("Изпрати", "Send"),
"action_share": ("Сподели", "Share"),
"loading": ("Зареждане…", "Loading…"),
"sync_never": ("никога", "never"),
"last_sync": ("Последна синхронизация: %1$s", "Last synced: %1$s"),
"offline_banner": ("Няма интернет връзка. Показваме последно наличните данни.", "No internet connection. Showing the last available data."),
"stale_banner": ("Временно не можем да обновим данните. Показваме последно наличните.", "We can’t refresh the data right now. Showing the last available data."),
"offline_global": ("Няма интернет връзка", "No internet connection"),
"demo_banner": ("ДЕМО ДАННИ — само за разработка, не са реални", "DEMO DATA — development only, not real"),
"error_title": ("Нещо се обърка", "Something went wrong"),
"error_offline_title": ("Няма връзка", "You’re offline"),
"error_network": ("Провери интернет връзката си и опитай отново.", "Check your internet connection and try again."),
"error_temporary": ("Временно не можем да заредим данните. Опитай отново след малко.", "We can’t load the data right now. Please try again shortly."),
"error_temporary_subject": ("Временно не можем да заредим %1$s. Опитай отново.", "We can’t load %1$s right now. Please try again."),
"error_unauthorized": ("Сесията е изтекла. Моля, влез отново.", "Your session has expired. Please sign in again."),
"error_not_found": ("Не открихме търсеното съдържание.", "We couldn’t find this content."),
"error_no_app": ("Няма приложение, което да отвори това.", "No app available to open this."),
"subject_news": ("новините", "the news"),
"subject_article": ("статията", "the article"),
"subject_catalog": ("каталога", "the catalogue"),
"subject_events": ("събитията", "the events"),
"subject_loans": ("заеманията", "your loans"),
"subject_membership": ("членството", "your membership"),
"na_title": ("Все още не е достъпно", "Not available yet"),
"na_login": ("Онлайн входът още не е включен от библиотеката. Когато бъде включен, функцията ще се появи тук автоматично. За достъп ще ти трябва ПИН, който се издава в библиотеката.", "Online sign-in hasn’t been switched on by the library yet. It will appear here automatically once it is. You will need a PIN issued at the library."),
"na_loans": ("Заетите книги и сроковете ще се показват тук, когато библиотечната система предостави онлайн достъп.", "Borrowed books and due dates will appear here once the library system offers online access."),
"na_membership": ("Данните за членството ще се показват тук, когато библиотечната система предостави онлайн достъп.", "Membership details will appear here once the library system offers online access."),
"na_holds": ("Заявяването на книги онлайн още не се поддържа. Попитай в библиотеката.", "Online holds aren’t supported yet. Please ask at the library."),
"na_renew": ("Онлайн подновяване още не се поддържа. Попитай в библиотеката.", "Online renewals aren’t supported yet. Please ask at the library."),
"na_password_reset": ("Нов ПИН се издава само в библиотеката — обърни се към библиотекаря.", "A new PIN is issued only at the library — please contact the librarian."),
"na_account_deletion": ("Искане за изтриване на данни се подава в библиотеката или по имейл.", "Data deletion requests are made at the library or by email."),
"na_push": ("Сървърни известия още не се поддържат.", "Server notifications aren’t supported yet."),
"status_available": ("Налична", "Available"),
"a11y_open_photo": ("Отвори снимката в цял екран", "Open photo full screen"),
"content_open_file": ("Отвори файла", "Open file"),
"status_on_loan": ("Заета", "On loan"),
"status_not_on_shelf": ("Не е на рафта", "Not on the shelf"),
"status_unavailable": ("Недостъпна", "Unavailable"),
"due_today": ("Срокът е днес", "Due today"),
"membership_active": ("Активно", "Active"),
"membership_expired": ("Изтекло", "Expired"),
"membership_suspended": ("Спряно", "Suspended"),
"membership_unknown": ("Неизвестно", "Unknown"),
"label_calendar": ("Календар", "Calendar"),
"label_library": ("Библиотека", "Library"),
"label_news": ("Новини", "News"),
"label_shelf": ("Витрина", "Showcase"),
"home_search_hint": ("Търси книга, новина, събитие…", "Search books, news, events…"),
"more_group_chitalishte": ("Читалището", "The centre"),
"more_group_app": ("Приложението", "The app"),
"home_upcoming": ("Предстоящи събития", "Upcoming events"),
"home_new_books": ("Нови постъпления", "New arrivals"),
"home_latest_news": ("Още новини", "More news"),
"news_search_hint": ("Търси в новините", "Search news"),
"news_saved": ("Запазени", "Saved"),
"news_empty": ("Няма новини по този критерий.", "No news match this filter."),
"news_no_saved": ("Все още нямаш запазени новини.", "You have no saved news yet."),
"article_gallery": ("Галерия", "Gallery"),
"photo_n": ("Снимка %1$d", "Photo %1$d"),
"events_title": ("Събития", "Events"),
"events_empty": ("Няма събития за показване", "No events to show"),
"events_empty_hint": ("Събитията идват от годишния календар на сайта на читалището.", "Events come from the annual calendar on the centre’s website."),
"events_calendar_view": ("Изглед календар", "Calendar view"),
"events_list_view": ("Изглед списък", "List view"),
"event_no_date": ("Без обявена дата", "Date not announced"),
"event_add_calendar": ("Добави в календара", "Add to calendar"),
"event_remind": ("Напомни ми", "Remind me"),
"event_remove_reminder": ("Премахни напомнянето", "Remove reminder"),
"event_source": ("Виж публикацията", "View the post"),
"reminder_set": ("Ще получиш напомняне преди събитието.", "You’ll get a reminder before the event."),
"reminder_removed": ("Напомнянето е премахнато.", "Reminder removed."),
"reminder_past": ("Събитието е минало — не може да се зададе напомняне.", "The event has passed — a reminder can’t be set."),
"calendar_prev": ("Предишен месец", "Previous month"),
"calendar_next": ("Следващ месец", "Next month"),
"calendar_weekdays": ("Пн,Вт,Ср,Чт,Пт,Сб,Нд", "Mo,Tu,We,Th,Fr,Sa,Su"),
"calendar_has_events": ("има събития", "has events"),
"calendar_today": ("днес", "today"),
"calendar_show_day": ("покажи събитията за деня", "show events for this day"),
"catalog_sort": ("Подреждане", "Sort"),
"catalog_filters": ("Филтри", "Filters"),
"catalog_search_hint": ("Търси по %1$s", "Search by %1$s"),
"catalog_no_results": ("Не открихме книги по това търсене.", "No books match this search."),
"catalog_no_results_hint": ("Опитай с друга дума, провери правописа или премахни филтрите.", "Try another word, check the spelling or remove filters."),
"catalog_data_as_of": ("данни към %1$s", "data as of %1$s"),
"field_all": ("Всичко", "Everything"),
"field_title": ("Заглавие", "Title"),
"field_author": ("Автор", "Author"),
"field_isbn": ("ISBN", "ISBN"),
"field_keyword": ("Ключова дума", "Keyword"),
"field_inventory": ("Инв. номер", "Inventory no."),
"sort_relevance": ("Най-подходящи", "Best match"),
"sort_title": ("Заглавие (А–Я)", "Title (A–Z)"),
"sort_author": ("Автор (А–Я)", "Author (A–Z)"),
"sort_year_desc": ("Година (нови първо)", "Year (newest first)"),
"sort_year_asc": ("Година (стари първо)", "Year (oldest first)"),
"sort_newest": ("Нови постъпления", "Recently added"),
"filter_all": ("Всички", "All"),
"filter_only_available": ("Само налични", "Available only"),
"filter_doc_type": ("Вид документ", "Document type"),
"filter_genre": ("Раздел (жанр)", "Section (genre)"),
"filter_department": ("Отдел", "Department"),
"filter_language": ("Език", "Language"),
"filter_year": ("Година на издаване", "Publication year"),
"book_status_as_of": ("Наличност към %1$s", "Availability as of %1$s"),
"book_hold": ("Заяви книгата", "Place a hold"),
"book_description": ("Описание", "Description"),
"book_details": ("Библиографски данни", "Bibliographic details"),
"book_other_authors": ("Други автори", "Other authors"),
"book_publisher": ("Издателство", "Publisher"),
"book_year": ("Година", "Year"),
"book_isbn": ("ISBN", "ISBN"),
"book_language": ("Език", "Language"),
"book_doc_type": ("Вид", "Type"),
"book_genre": ("Раздел", "Section"),
"book_keywords": ("Ключови думи", "Keywords"),
"book_call_number": ("Сигнатура", "Call number"),
"book_inventory": ("Инвентарен номер", "Inventory number"),
"book_department": ("Отдел", "Department"),
"book_library": ("Библиотека", "Library"),
"book_availability_note": ("Наличността идва от публичния каталог на библиотеката (InvLib) и се обновява, когато библиотекарят запише промени. „Не е на рафта“ означава, че книгата е заета или временно недостъпна.", "Availability comes from the library’s public catalogue (InvLib) and updates when the librarian saves changes. “Not on the shelf” means the item is on loan or temporarily unavailable."),
"book_same_author": ("От същия автор", "By the same author"),
"share_book_footer": ("Електронен каталог: %1$s", "Online catalogue: %1$s"),
"hold_ok_title": ("Заявката е изпратена", "Hold placed"),
"hold_ok_text": ("Библиотеката ще отдели книгата за теб.", "The library will set the book aside for you."),
"hold_error_title": ("Заявката не е изпратена", "Hold not placed"),
"hold_login_needed": ("За да заявиш книга, влез в профила си.", "Sign in to place a hold."),
"my_section_library": ("Библиотека", "Library"),
"my_card": ("Читателска карта", "Library card"),
"my_card_desc": ("Покажи картата на гишето", "Show your card at the desk"),
"my_card_self_desc": ("Въведена от теб карта", "Card you entered"),
"my_card_add_desc": ("Добави номера от своята карта", "Add the number from your card"),
"my_books": ("Моите книги", "My books"),
"my_books_desc": ("Заети книги и срокове за връщане", "Borrowed books and due dates"),
"my_membership": ("Моето членство", "My membership"),
"my_membership_desc": ("Статус и валидност", "Status and validity"),
"my_profile": ("Моят профил", "My profile"),
"my_profile_desc": ("Читателски данни", "Reader details"),
"my_notifications": ("Известия", "Notifications"),
"my_notifications_desc": ("Срокове, събития, новини", "Due dates, events, news"),
"guest_title": ("Добре дошъл!", "Welcome!"),
"guest_text": ("Новините, събитията, каталогът и информацията за читалището са достъпни и без вход.", "News, events, the catalogue and information about the centre are available without signing in."),
"reader_number": ("Читателски № %1$s", "Reader no. %1$s"),
"logout_title": ("Изход от профила?", "Sign out?"),
"logout_text": ("Сесията и всички запазени на устройството читателски данни ще бъдат изтрити.", "Your session and all reader data stored on this device will be deleted."),
"login_title": ("Вход", "Sign in"),
"login_heading": ("Вход за читатели", "Reader sign-in"),
"login_card": ("Читателски номер", "Reader number"),
"login_password": ("ПИН (от библиотеката)", "PIN (from the library)"),
"login_show_password": ("Покажи ПИН-а", "Show PIN"),
"login_hide_password": ("Скрий ПИН-а", "Hide PIN"),
"login_remember": ("Запомни ме на това устройство", "Keep me signed in on this device"),
"login_forgot": ("Забравен ПИН", "Forgot PIN"),
"login_reset_sent": ("Ако номерът е регистриран, ще получиш инструкции.", "If the number is registered, you’ll receive instructions."),
"login_wrong": ("Грешен читателски номер или ПИН.", "Wrong reader number or PIN."),
"login_privacy_note": ("ПИН-ът не се съхранява на устройството. Сесията се пази шифрована с ключ от Android Keystore.", "Your PIN is never stored on the device. The session is kept encrypted with an Android Keystore key."),
"login_demo_hint": ("Демо вход: DEMO-0001 / demo", "Demo sign-in: DEMO-0001 / demo"),
"card_add_manual": ("Добави номера на картата си", "Add your card number"),
"card_add_title": ("Дигитална карта", "Digital card"),
"card_add_text": ("Въведи номера от своята читателска карта. Приложението ще покаже баркод в същия формат (Code 39), в който библиотеката печата картите.", "Enter the number from your library card. The app shows a barcode in the same format (Code 39) the library prints on cards."),
"card_number_label": ("Номер на картата", "Card number"),
"card_number_hint": ("Главни латински букви, цифри и „-“", "Capital Latin letters, digits and “-”"),
"card_number_invalid": ("Номерът съдържа неразрешени знаци.", "The number contains invalid characters."),
"card_name_label": ("Име (по желание)", "Name (optional)"),
"card_show": ("Покажи картата", "Show card"),
"card_self_note": ("Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устройство.", "You entered this number; it hasn’t been verified by the library. It is stored encrypted on this device only."),
"card_usage_note": ("Покажи картата на библиотекаря; баркодът може да се сканира от четеца на гишето.", "Show the card to the librarian; the barcode can be scanned at the desk."),
"card_remove_title": ("Да премахна ли картата от устройството?", "Remove the card from this device?"),
"card_login_instead": ("Вход с библиотечни данни", "Sign in with library credentials"),
"card_library_line": ("ЧИТАТЕЛСКА КАРТА", "LIBRARY CARD"),
"card_barcode_desc": ("Баркод на карта %1$s", "Barcode for card %1$s"),
"card_qr_desc": ("QR код на картата", "Card QR code"),
"card_flip": ("Обърни картата", "Flip the card"),
"card_flip_hint": ("Докосни картата, за да видиш номера едро", "Tap the card to see the number in large print"),
"widget_label": ("Читалище Яворец", "Chitalishte Yavorets"),
"widget_description": ("Следващо събитие и срок за връщане", "Next event and return due date"),
"widget_next_event": ("Следващо събитие", "Next event"),
"widget_no_events": ("Няма предстоящи събития", "No upcoming events"),
"widget_due": ("Срок: %1$s — %2$s", "Due: %1$s — %2$s"),
"loans_empty": ("Нямаш заети книги", "You have no borrowed books"),
"loans_empty_hint": ("Разгледай каталога — може би нещо ще те заинтригува.", "Browse the catalogue — something might catch your eye."),
"loan_borrowed": ("Заета: %1$s", "Borrowed: %1$s"),
"loan_due": ("Връщане: %1$s", "Due: %1$s"),
"loan_renew": ("Поднови", "Renew"),
"loan_stale_note": ("Последно синхронизирани данни — може да не са актуални.", "Last synced data — may be out of date."),
"renew_ok": ("Подновено до %1$s.", "Renewed until %1$s."),
"membership_number": ("Членски номер", "Member number"),
"membership_since": ("Член от", "Member since"),
"membership_valid_until": ("Валидно до", "Valid until"),
"profile_photo": ("Профилна снимка", "Profile photo"),
"profile_reader_number": ("Читателски номер", "Reader number"),
"profile_category": ("Категория", "Category"),
"profile_email": ("Имейл", "Email"),
"profile_registered": ("Регистриран на", "Registered on"),
"profile_edit_note": ("Личните данни се поддържат от библиотеката. За промяна се обърни към библиотекаря.", "Personal details are managed by the library. Please ask the librarian to change them."),
"profile_delete_request": ("Искане за изтриване на данните", "Request data deletion"),
"profile_delete_text": ("Библиотеката ще получи искане да изтрие читателските ти данни съгласно ОРЗД. Заетите книги трябва да бъдат върнати.", "The library will receive a request to delete your reader data under the GDPR. Borrowed items must be returned first."),
"profile_delete_sent": ("Искането е изпратено.", "Request sent."),
"activities_title": ("Дейности", "Activities"),
"activities_intro": ("Дейностите, групите, проектите и историята на читалището — съдържанието идва директно от сайта chyavorec.org.", "The centre’s activities, groups, projects and history — content comes straight from chyavorec.org."),
"activities_empty": ("Няма открити страници.", "No pages found."),
"page_empty": ("Страницата няма съдържание за показване.", "This page has no content to show."),
"page_source": ("Източник: %1$s", "Source: %1$s"),
"gallery_title": ("Галерия", "Gallery"),
"gallery_empty": ("Няма снимки за показване.", "No photos to show."),
"contacts_title": ("Контакти", "Contacts"),
"contacts_call": ("Обади се", "Call"),
"contacts_email": ("Изпрати имейл", "Send email"),
"contacts_map": ("Отвори карта", "Open map"),
"contacts_site": ("Отвори сайта", "Open website"),
"contacts_address": ("Адрес", "Address"),
"contacts_see_site": ("Виж страницата „Контакти“ в сайта", "See the Contacts page on the website"),
"contacts_phone": ("Телефон", "Phone"),
"contacts_email_label": ("Имейл", "Email"),
"contacts_hours": ("Работно време", "Opening hours"),
"contacts_hours_unknown": ("Не е обявено в сайта", "Not listed on the website"),
"contacts_web": ("В интернет", "Online"),
"contacts_fallback_note": ("Страницата „Контакти“ не беше заредена — показани са само основните данни. Опитай отново по-късно.", "The Contacts page couldn’t be loaded — only basic details are shown. Please try again later."),
"contacts_source": ("Данни от chyavorec.org", "Data from chyavorec.org"),
"more_events_desc": ("Календар на читалището", "Community centre calendar"),
"more_activities_desc": ("Групи, клубове, проекти", "Groups, clubs, projects"),
"more_about_desc": ("История и мисия", "History and mission"),
"more_digital_desc": ("Услуги, обучения, работно време", "Services, training, hours"),
"more_gallery_desc": ("Снимки от сайта", "Photos from the website"),
"more_contacts_desc": ("Адрес, телефон, карта", "Address, phone, map"),
"search_hint": ("Книги, автори, новини, събития…", "Books, authors, news, events…"),
"search_books": ("Книги", "Books"),
"search_authors": ("Автори", "Authors"),
"search_pages": ("Страници", "Pages"),
"search_recent": ("Скорошни търсения", "Recent searches"),
"search_start": ("Какво търсиш?", "What are you looking for?"),
"search_start_hint": ("Търсенето обхваща каталога, новините, събитията и страниците на читалището.", "Search covers the catalogue, news, events and the centre’s pages."),
"search_no_results": ("Нищо не открихме", "No results"),
"settings_title": ("Настройки", "Settings"),
"settings_appearance": ("Изглед", "Appearance"),
"settings_theme": ("Тема", "Theme"),
"settings_dynamic": ("Цветове от тапета", "Wallpaper colours"),
"settings_dynamic_desc": ("Material You вместо цветовете на читалището", "Material You instead of the centre’s colours"),
"settings_language": ("Език", "Language"),
"settings_notifications": ("Известия", "Notifications"),
"settings_notifications_desc": ("Какви известия да получаваш", "Choose which notifications you get"),
"settings_data": ("Данни", "Data"),
"settings_clear_cache": ("Изчисти кеша", "Clear cache"),
"settings_clear_cache_desc": ("Изтрива запазените новини, каталог и снимки", "Deletes cached news, catalogue and images"),
"settings_clear_cache_confirm": ("Запазените офлайн данни ще бъдат изтрити и заредени отново при следваща връзка. Входът и картата остават.", "Offline data will be deleted and reloaded next time you’re online. Your sign-in and card stay."),
"settings_cache_cleared": ("Кешът е изчистен.", "Cache cleared."),
"settings_legal": ("Информация", "Information"),
"theme_system": ("Както системата", "System default"),
"theme_light": ("Светла", "Light"),
"theme_dark": ("Тъмна", "Dark"),
"language_system": ("Както системата", "System default"),
"notifications_intro": ("Известията се подготвят на самото устройство — без рекламни или проследяващи услуги.", "Notifications are prepared on the device itself — no advertising or tracking services."),
"notifications_loans_na": ("Ще работи, когато библиотечната система предостави онлайн достъп.", "Will work once the library system offers online access."),
"notifications_library_desc": ("Съобщения от библиотеката и промени в членството", "Library messages and membership changes"),
"notifications_system": ("Системни настройки за известия", "System notification settings"),
"notifications_push_note": ("Сървърни (push) известия ще бъдат добавени, когато читалището въведе сървър за тях. Дотогава приложението проверява за новини и срокове периодично.", "Server (push) notifications will be added once the centre sets up a server for them. Until then the app checks for news and due dates periodically."),
"channel_loans": ("Срокове за връщане", "Due dates"),
"channel_loans_desc": ("Наближаващ или изтекъл срок на заета книга", "An upcoming or passed due date for a borrowed book"),
"channel_events": ("Събития", "Events"),
"channel_events_desc": ("Напомняния за събития", "Event reminders"),
"channel_news": ("Новини", "News"),
"channel_news_desc": ("Нова публикация в сайта", "A new post on the website"),
"channel_library": ("Библиотека", "Library"),
"channel_library_desc": ("Съобщения от библиотеката", "Messages from the library"),
"channel_updates": ("Обновления", "Updates"),
"channel_updates_desc": ("Нови версии на приложението", "New versions of the app"),
"settings_updates": ("Обновления", "Updates"),
"update_auto": ("Автоматично обновяване", "Automatic updates"),
"update_auto_desc": ("Проверка при отваряне и два пъти дневно; изтегляне по Wi-Fi; инсталиране, докато приложението не се използва (Android 12+), или с едно докосване", "Checked on launch and twice a day; downloaded over Wi-Fi; installed while the app isn’t in use (Android 12+) or with a single tap"),
"update_check_now": ("Провери за нова версия", "Check for updates"),
"update_status_checking": ("Проверка за нова версия…", "Checking for updates…"),
"update_status_current": ("Имаш последната версия (%1$s)", "You have the latest version (%1$s)"),
"update_status_available": ("Налична е версия %1$s", "Version %1$s is available"),
"update_status_downloading": ("Изтегляне на новата версия…", "Downloading the new version…"),
"update_status_ready": ("Версия %1$s е готова за инсталиране", "Version %1$s is ready to install"),
"update_disabled_play": ("Приложението се обновява автоматично от Google Play.", "The app is updated automatically by Google Play."),
"update_disabled_dev": ("Автоматичното обновяване работи в официалната версия от страницата с версиите.", "Automatic updates work in the official build from the releases page."),
"update_dialog_title": ("Нова версия %1$s", "New version %1$s"),
"update_dialog_available": ("Излезе нова версия на приложението.", "A new version of the app is available."),
"update_size": ("Размер: %1$s", "Size: %1$s"),
"update_notes": ("Какво ново", "What’s new"),
"update_download": ("Изтегли и инсталирай", "Download and install"),
"update_install": ("Инсталирай", "Install"),
"update_later": ("По-късно", "Later"),
"update_hide": ("Скрий", "Hide"),
"update_progress": ("%1$d%%", "%1$d%%"),
"update_ready": ("Новата версия е изтеглена и проверена (контролна сума и подпис).", "The new version has been downloaded and verified (checksum and signature)."),
"update_permission": ("За да се обнови, разреши на „Читалище Яворец“ да инсталира приложения, след което се върни тук.", "To update, allow “Chitalishte Yavorets” to install apps, then come back here."),
"update_permission_action": ("Към настройките", "Open settings"),
"update_installing": ("Инсталиране… Приложението ще се затвори за момент.", "Installing… The app will close for a moment."),
"update_error_check": ("Проверката за нова версия не успя. Опитай отново по-късно.", "Couldn’t check for updates. Please try again later."),
"update_error_network": ("Изтеглянето не успя. Провери връзката и опитай отново.", "The download failed. Check your connection and try again."),
"update_error_checksum": ("Изтегленият файл не премина проверката и беше изтрит. Опитай отново.", "The downloaded file failed verification and was deleted. Please try again."),
"update_error_signature": ("Новата версия е подписана с различен ключ и не може да замени инсталираната. Изтегли я ръчно от страницата на версията (след деинсталиране на текущата).", "The new version is signed with a different key and can’t replace the installed one. Download it manually from the release page (after uninstalling the current one)."),
"update_error_install": ("Инсталирането не беше завършено.", "The installation wasn’t completed."),
"update_open_release": ("Страница на версията", "Release page"),
"notif_update_available": ("Налична е нова версия %1$s", "New version %1$s available"),
"notif_update_ready": ("Версия %1$s е готова за инсталиране", "Version %1$s is ready to install"),
"notif_update_text": ("Докосни, за да обновиш приложението.", "Tap to update the app."),
"channel_messages": ("Съобщения от читалището", "Messages from the centre"),
"channel_messages_desc": ("Съобщения, изпратени от читалището до потребителите на приложението", "Messages the centre sends to app users"),
"channel_messages_important": ("Важни съобщения", "Important messages"),
"channel_messages_important_desc": ("Съобщения, отбелязани от читалището като важни", "Messages the centre marks as important"),
"messages_title": ("Съобщения", "Messages"),
"messages_empty": ("Няма съобщения от читалището", "No messages from the centre"),
"messages_members_hint": ("Някои съобщения са само за членове. Въведи читателската си карта (или влез) в „Моето“, за да ги виждаш.", "Some messages are for members only. Enter your library card (or sign in) under “My” to see them."),
"messages_members_only": ("За членове", "Members"),
"messages_new": ("Ново", "New"),
"messages_valid_until": ("Валидно до %1$s", "Valid until %1$s"),
"messages_open_link": ("Отвори връзката", "Open link"),
"more_messages_desc": ("Съобщения и покани от читалището", "Announcements and invitations from the centre"),
"notif_new_article": ("Нова публикация", "New post"),
"notif_due_soon": ("Наближава срок за връщане", "Due date approaching"),
"notif_overdue": ("Просрочена книга", "Overdue book"),
"notif_event_reminder": ("Напомняне за събитие", "Event reminder"),
"onb_skip": ("Пропусни", "Skip"),
"onb_next": ("Напред", "Next"),
"onb_login": ("Вход с читателски данни", "Sign in as a reader"),
"onb_guest": ("Продължи без вход", "Continue as guest"),
"onb_welcome_title": ("Читалище Яворец", "Chitalishte Yavorets"),
"onb_welcome_text": ("Официалното приложение на читалището — новини, събития, библиотека и дейности на едно място, винаги актуални от сайта chyavorec.org.", "The centre’s official app — news, events, library and activities in one place, always up to date from chyavorec.org."),
"onb_catalog_title": ("Библиотечен каталог", "Library catalogue"),
"onb_catalog_text": ("Търси сред книгите на библиотеката по заглавие, автор, ключова дума или инвентарен номер и виж дали са налични.", "Search the library’s books by title, author, keyword or inventory number and see if they’re available."),
"onb_card_title": ("Читателска карта", "Library card"),
"onb_card_text": ("Носи картата си в телефона — с баркод, който се сканира на гишето.", "Keep your card on your phone — with a barcode that can be scanned at the desk."),
"onb_books_title": ("Моите книги", "My books"),
"onb_books_text": ("Сроковете за връщане с цветен индикатор и напомняне навреме — щом библиотечната система го позволи.", "Due dates with a colour indicator and timely reminders — once the library system allows it."),
"onb_news_title": ("Новини и събития", "News and events"),
"onb_news_text": ("Концерти, празници, срещи и всичко, което се случва в читалището — с напомняне и добавяне в календара.", "Concerts, celebrations, meetings and everything happening at the centre — with reminders and calendar sync."),
"home_carousel": ("Последни новини", "Latest news"),
"stat_years": ("години история", "years of history"),
"stat_books": ("документа в каталога", "catalogue items"),
"stat_events": ("събития през месеца", "events this month"),
"event_recurring": ("Събитие от годишния календар на читалището — повтаря се всяка година на тази дата.", "An event from the centre’s annual calendar — it recurs every year on this date."),
"contacts_persons": ("Лица за контакт", "Contact persons"),
"documents_title": ("Документи и архив", "Documents & archive"),
"documents_tab_docs": ("Документи", "Documents"),
"documents_tab_publications": ("Исторически публикации", "Historical articles"),
"documents_empty": ("Няма документи за показване.", "No documents to show."),
"more_documents_desc": ("Устав, отчети, юбилейният вестник", "Statute, reports, jubilee newspaper"),
"catalog_title": ("Електронен каталог", "Online catalogue"),
"catalog_powered_by": ("данни от InvLib", "powered by InvLib"),
"about_invlib_desc": ("Безплатната библиотечна система с отворен код, с която работи библиотеката. Оттук идва каталогът.", "The free, open-source library system the library uses. The catalogue comes from it."),
"about_catalog_desc": ("Книгите на библиотеката с актуална наличност — търсене по заглавие, автор, ключова дума.", "The library’s books with current availability — search by title, author, keyword."),
"about_creator": ("Създател", "Created by"),
"privacy_title": ("Политика за поверителност", "Privacy policy"),
"terms_title": ("Условия за използване", "Terms of use"),
"legal_site_version": ("Виж и: %1$s", "See also: %1$s"),
"about_title": ("За приложението", "About the app"),
"about_version": ("Версия %1$s", "Version %1$s"),
"about_version_full": ("Версия %1$s (%2$d)", "Version %1$s (%2$d)"),
"about_env": ("Среда: %1$s · демо данни: %2$s", "Environment: %1$s · demo data: %2$s"),
"about_invlib": ("Библиотечната система InvLib", "The InvLib library system"),
}

PRIVACY_BG = """# Кой обработва данните

Приложението „Читалище Яворец“ е официалното мобилно приложение на Народно читалище „Васил Левски – 1922“, с. Яворец, общ. Габрово (наричано по-долу „читалището“). Връзка: chitalishte_yavorec@abv.bg.

# Принцип

Приложението събира възможно най-малко данни. То няма реклами, няма анализ на поведението (analytics), няма услуги за проследяване и не изпраща отчети за сривове към трети страни.

# Какви данни се обработват

Публично съдържание: новините, събитията, снимките и страниците се зареждат от сайта chyavorec.org, а библиотечният каталог — от публичния файл katalog.json на системата InvLib, хостван в GitHub (raw.githubusercontent.com и cdn.jsdelivr.net). При всяка такава заявка съответният сървър вижда IP адреса на устройството, както при посещение на обикновен уебсайт. Каталогът не съдържа лични данни на читатели.

Съобщения от читалището: приложението проверява на около час публичния файл със съобщения на chyavorec.org. Съобщенията „само за членове“ се показват, ако си въвел читателска карта или си влязъл — тази проверка е на устройството и нищо не се изпраща към сайта. Прочетените съобщения се помнят само на устройството.

Обновления: версията, инсталирана извън Google Play, проверява за нова версия във файла update.json в GitHub (github.com) и изтегля новата версия оттам. Изпраща се само обикновена заявка (IP адресът е видим за GitHub); не се изпращат лични данни. Автоматичното обновяване се изключва от Настройки → Обновления. Версията от Google Play се обновява от Google Play.

Данни само на устройството: настройки (тема, език, известия), запазени новини, напомняния за събития, история на търсенията и кеширано публично съдържание за офлайн работа. Те не напускат устройството и не се архивират в облака.

Читателска карта: ако въведеш номера на своята карта, той (и името, ако го въведеш) се пази шифрован с ключ от Android Keystore само на това устройство.

Вход (само ако си дал съгласие в библиотеката и имаш издаден ПИН): номерът на читателската карта и ПИН-ът се изпращат само по HTTPS към сървъра на читалището (chyavorec.org) и ПИН-ът никога не се записва. При вход се изпраща и моделът на телефона (напр. „Pixel 8“), за да се разпознава сесията. Пази се само шифрован токен за сесията. Името ти, заетите книги, сроковете и членството идват от този сървър, който получава от библиотечната програма само данните на съгласилите се читатели (без ЕГН, адрес и телефон), и се кешират шифровано на устройството. Съгласието се оттегля в библиотеката — тогава данните ти се изтриват от сървъра при следващото обновяване.

Защита на входа: след няколко неуспешни опита за вход входът временно се блокира (на устройството и на сървъра), за да не може ПИН-ът да бъде отгатнат.

Уиджет на началния екран: ако го добавиш, той показва само броя книги за връщане и най-близкия срок — без заглавия и без име.

# Известия

Известията се подготвят на устройството (периодична проверка за нови публикации и срокове). Не се използват сървъри за push известия.

# Твоите права

Можеш по всяко време да изтриеш данните на устройството: „Изход“ изтрива сесията и читателските данни; „Премахни“ изтрива въведената карта; „Изчисти кеша“ изтрива публичното съдържание; деинсталирането премахва всичко. Изтриване на профила за приложението и данните ти от сървъра можеш да поискаш на https://chyavorec.org/app-delete, в читалището или по имейл. За данните, които библиотеката поддържа за теб като читател (по Наредба № 3 и ОРЗД), можеш да поискаш достъп, корекция или изтриване в читалището или по имейл. Имаш право на жалба до Комисията за защита на личните данни (www.cpdp.bg).

# Деца

Приложението не е насочено към деца под 13 години. То може да се използва без вход и без никакви лични данни.

# Промени

При промяна на тази политика новата версия се публикува в приложението и в сайта на читалището."""

PRIVACY_EN = """# Who processes the data

“Chitalishte Yavorets” is the official mobile app of the “Vasil Levski – 1922” Community Centre (chitalishte), Yavorets village, Gabrovo municipality, Bulgaria (“the centre”). Contact: chitalishte_yavorec@abv.bg.

# Principle

The app collects as little data as possible. It has no ads, no behavioural analytics, no tracking services and does not send crash reports to third parties.

# What data is processed

Public content: news, events, photos and pages are loaded from chyavorec.org, and the library catalogue from the public katalog.json file of the InvLib system hosted on GitHub (raw.githubusercontent.com and cdn.jsdelivr.net). Each such server sees the device’s IP address, as with any website visit. The catalogue contains no reader data.

Messages from the centre: about once an hour the app checks the public messages file on chyavorec.org. Members-only messages are shown if you have entered your library card or signed in — this check happens on the device and nothing is sent to the website. Read messages are remembered only on the device.

Updates: the version installed outside Google Play checks for a new version in the update.json file on GitHub (github.com) and downloads it from there. Only a plain request is sent (GitHub sees the IP address); no personal data is sent. Automatic updates can be turned off in Settings → Updates. The Google Play version is updated by Google Play.

Data kept only on the device: settings (theme, language, notifications), saved news, event reminders, search history and cached public content for offline use. It never leaves the device and is not backed up to the cloud.

Library card: if you enter your card number, it (and your name, if entered) is stored encrypted with an Android Keystore key on this device only.

Sign-in (only if you have given consent at the library and have been issued a PIN): your library card number and PIN are sent only over HTTPS to the centre’s server (chyavorec.org), and the PIN is never stored. The phone’s model name (e.g. “Pixel 8”) is also sent at sign-in so that the session can be recognised. Only an encrypted session token is kept. Your name, borrowed items, due dates and membership come from this server, which receives from the library software only the data of readers who have consented (no personal ID number (EGN), address or phone number), and are cached encrypted on the device. Consent is withdrawn at the library — your data is then deleted from the server at the next sync.

Sign-in protection: after several failed sign-in attempts, sign-in is temporarily blocked (on the device and on the server) so that the PIN cannot be guessed.

Home-screen widget: if you add it, it shows only the number of books due and the nearest due date — no titles and no name.

# Notifications

Notifications are prepared on the device (periodic checks for new posts and due dates). No push notification servers are used.

# Your rights

You can delete the data on the device at any time: “Sign out” deletes the session and reader data; “Remove” deletes the entered card; “Clear cache” deletes public content; uninstalling removes everything. You can request deletion of your app account and your data on the server at https://chyavorec.org/app-delete, at the centre or by email. For the data the library keeps about you as a reader (under Bulgarian Ordinance No. 3 and the GDPR), you can request access, correction or deletion at the centre or by email. You may lodge a complaint with the Bulgarian Commission for Personal Data Protection (www.cpdp.bg).

# Children

The app is not directed at children under 13. It can be used without signing in and without any personal data.

# Changes

If this policy changes, the new version is published in the app and on the centre’s website."""

TERMS_BG = """# Предназначение

Приложението предоставя информация за дейността на НЧ „Васил Левски – 1922“, с. Яворец, и достъп до публичния каталог на библиотеката. Използването е безплатно.

# Съдържание

Новините, събитията, снимките и текстовете са собственост на читалището или на посочените автори и се зареждат от сайта chyavorec.org. Споделянето им е позволено с връзка към източника.

# Наличност на книгите

Наличността в каталога е информативна и отразява последното публикуване от библиотечната система. Окончателното потвърждение се дава в библиотеката.

# Отговорност

Читалището полага грижа данните да са точни, но не носи отговорност за временна недостъпност на услугата или на външните сайтове.

# Контакт

chitalishte_yavorec@abv.bg"""

TERMS_EN = """# Purpose

The app provides information about the activities of the “Vasil Levski – 1922” Community Centre, Yavorets, and access to the library’s public catalogue. It is free to use.

# Content

News, events, photos and texts belong to the centre or the credited authors and are loaded from chyavorec.org. Sharing is allowed with a link to the source.

# Book availability

Catalogue availability is informative and reflects the latest publication by the library system. Final confirmation is given at the library.

# Liability

The centre takes care that the data is accurate but is not liable for temporary unavailability of the service or of external websites.

# Contact

chitalishte_yavorec@abv.bg"""

ABOUT_BG = """Официалното мобилно приложение на Народно читалище „Васил Левски – 1922“, с. Яворец — основано на 17 декември 1922 г.

# Откъде идват данните

Новини, събития, снимки, документи и страници — направо от данните, които публикува сайтът chyavorec.org. Библиотечният каталог — от публичния каталог на системата InvLib, която библиотеката използва. Нищо не се въвежда два пъти: служителите поддържат съдържанието само в сайта и в InvLib.

# Поверителност

Без реклами, без проследяване, без анализ на поведението. Личните данни на устройството се пазят шифровани."""

ABOUT_EN = """The official mobile app of the “Vasil Levski – 1922” Community Centre in Yavorets — founded on 17 December 1922.

# Where the data comes from

News, events, photos, documents and pages — straight from the data published by chyavorec.org. The library catalogue — from the public catalogue of the InvLib system the library uses. Nothing is entered twice: staff maintain content only on the website and in InvLib.

# Privacy

No ads, no tracking, no behavioural analytics. Personal data on the device is stored encrypted."""

S["privacy_body"] = (PRIVACY_BG, PRIVACY_EN)
S["terms_body"] = (TERMS_BG, TERMS_EN)
S["about_body"] = (ABOUT_BG, ABOUT_EN)

P = {
"book_available_copies": (("%1$d наличен", "%1$d налични"), ("%1$d available", "%1$d available")),
"book_copies": (("%1$d екземпляр", "%1$d екземпляра"), ("%1$d copy", "%1$d copies")),
"catalog_count": (("%1$d резултат", "%1$d резултата"), ("%1$d result", "%1$d results")),
"catalog_show_results": (("Покажи %1$d резултат", "Покажи %1$d резултата"), ("Show %1$d result", "Show %1$d results")),
"due_days_left": (("остава %1$d ден", "остават %1$d дни"), ("%1$d day left", "%1$d days left")),
"due_overdue_days": (("просрочена с %1$d ден", "просрочена с %1$d дни"), ("%1$d day overdue", "%1$d days overdue")),
"messages_unread": (("%1$d непрочетено съобщение", "%1$d непрочетени съобщения"), ("%1$d unread message", "%1$d unread messages")),
"photos_count": (("%1$d снимка", "%1$d снимки"), ("%1$d photo", "%1$d photos")),
"error_rate_limited": (("Твърде много опити. Опитай отново след %1$d секунда.", "Твърде много опити. Опитай отново след %1$d секунди."), ("Too many attempts. Try again in %1$d second.", "Too many attempts. Try again in %1$d seconds.")),
"home_catalog_footer": (("Каталогът съдържа %1$d документ · данни към %2$s", "Каталогът съдържа %1$d документа · данни към %2$s"), ("The catalogue lists %1$d item · data as of %2$s", "The catalogue lists %1$d items · data as of %2$s")),
}

def esc(s):
    s = s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
    s = s.replace("\\", "\\\\").replace("'", "\\'").replace('"', '\\"')
    s = s.replace("\n", "\\n")
    if s.startswith("@") or s.startswith("?"):
        s = "\\" + s
    return s

def write(path, idx):
    out = ['<?xml version="1.0" encoding="utf-8"?>', '<resources>']
    for k in sorted(S):
        v = S[k][idx]
        fmt = "%1$" in v or "%2$" in v
        attr = '' if fmt else ''
        out.append('    <string name="%s"%s>%s</string>' % (k, attr, esc(v)))
    for k in sorted(P):
        one, other = P[k][idx]
        out.append('    <plurals name="%s">' % k)
        out.append('        <item quantity="one">%s</item>' % esc(one))
        out.append('        <item quantity="other">%s</item>' % esc(other))
        out.append('    </plurals>')
    out.append('</resources>')
    open(path, 'w', encoding='utf-8').write("\n".join(out) + "\n")

import sys
import subprocess
keys = set(subprocess.run("grep -rhoE 'R\\.string\\.[a-zA-Z0-9_]+' app/src | sed 's/R.string.//' | sort -u", shell=True, capture_output=True, text=True).stdout.split())
keys.discard('app_name')
missing = keys - set(S)
extra = set(S) - keys
print("missing:", sorted(missing))
print("unused:", sorted(extra))
base = 'app/src/main/res/'
write(base + 'values/strings.xml', 0)
write(base + 'values-en/strings.xml', 1)
