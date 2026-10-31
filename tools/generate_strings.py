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
"tab_my": ("Моето", "My library"),
"tab_news": ("Новини", "News"),
"tab_more": ("Още", "More"),
"qa_card": ("Читателска карта", "Library card"),
"qa_about": ("За читалището", "About us"),
"qa_contacts": ("Контакти", "Contacts"),
"qa_scan": ("Сканирай", "Scan"),
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
"action_undo": ("Отмени", "Undo"),
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
"subject_history": ("историята на четенето", "your reading history"),
"renew_requested": ("Заявката е изпратена. Библиотеката ще удължи срока при следващото обновяване на програмата си.", "Request sent. The library will extend the due date at its program’s next update."),
"loan_renew_pending": ("Заявено удължаване · чака библиотеката", "Renewal requested · waiting for the library"),
"loan_renew_rejected": ("Удължаването е отказано: %1$s", "Renewal declined: %1$s"),
"loan_renew_rejected_noreason": ("Удължаването е отказано.", "Renewal declined."),
"loan_renew_done": ("Удължено ✓", "Renewed ✓"),
"na_history": ("Историята на четенето още не е включена от библиотеката.", "Reading history isn’t switched on by the library yet."),
"na_messages": ("Личните съобщения още не са включени от библиотеката.", "Personal messages aren’t switched on by the library yet."),
"renew_conflict_pending": ("Удължаването вече е заявено и чака библиотеката.", "A renewal is already requested and waiting for the library."),
"renew_conflict_not_allowed": ("Тази книга не може да се удължи онлайн. Попитай в библиотеката.", "This book can’t be renewed online. Please ask at the library."),
"renew_conflict_other": ("Удължаването не беше прието. Опитай отново по-късно.", "The renewal wasn’t accepted. Please try again later."),
"history_title": ("Прочетени книги", "Books read"),
"my_history_desc": ("Историята на четенето и подобни книги", "Your reading history and similar books"),
"history_empty": ("Още няма прочетени книги", "No books read yet"),
"history_empty_hint": ("Върнатите книги ще се появят тук след следващото обновяване от библиотеката.", "Returned books will appear here after the library’s next update."),
"history_year_all": ("Всички години", "All years"),
"history_source_note": ("Историята идва от библиотеката само за читатели, дали съгласие.", "History comes from the library only for readers who gave consent."),
"history_similar": ("Подобни книги", "Similar books"),
"history_similar_note": ("Подбрани на телефона по раздел и автор — нищо не се изпраща.", "Picked on your phone by section and author — nothing is sent anywhere."),
"reading_title": ("Моето четене", "My reading"),
"reading_months_label": ("Книги по месеци", "Books per month"),
"reading_sections_label": ("По раздели", "By section"),
"reading_sections_other": ("Други", "Other"),
"reading_sections_cd": ("Книги по раздели: %1$s", "Books by section: %1$s"),
"reading_months_none_cd": ("Няма взети книги през последните 12 месеца", "No books borrowed in the last 12 months"),
"reading_open_history": ("Цялата история", "Full history"),
"reading_total": ("общо", "in total"),
"reading_this_year": ("тази година", "this year"),
"reading_favourite_author": ("любим автор", "favourite author"),
"reading_streak": ("най-дълга поредица", "longest streak"),
"reading_note": ("Изчислено на телефона от историята ти — нищо не се изпраща.", "Worked out on your phone from your history — nothing is sent anywhere."),
"history_range": ("взета %1$s · върната %2$s", "borrowed %1$s · returned %2$s"),
"history_range_open": ("взета %1$s · още не е върната", "borrowed %1$s · not returned yet"),
"scan_action": ("Сканирай баркод", "Scan barcode"),
"scan_title": ("Сканиране", "Scan"),
"scan_intro_title": ("Сканирай баркода на книгата", "Scan the book’s barcode"),
"scan_intro_text": ("Баркодът с ISBN на корицата или етикетът с инвентарния номер. Скенерът на Google Play услугите се отваря над приложението — то получава само разчетения номер.", "The ISBN barcode on the cover or the inventory label. The Google Play services scanner opens on top of the app — the app only receives the number it reads."),
"scan_unavailable_title": ("Скенерът не е достъпен", "The scanner isn’t available"),
"scan_unavailable": ("Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко.", "The barcode scanner comes with Google Play services — they’re missing here, or the scanner is still downloading. Enter the number manually or try again in a moment."),
"scan_catalog_loading": ("Зареждаме каталога…", "Loading the catalogue…"),
"scan_catalog_unavailable": ("Каталогът не се зареди — няма връзка. Ще опитаме пак, щом се свържеш.", "The catalogue didn’t load — no connection. We’ll try again once you’re online."),
"scan_code_waiting": ("Запазихме %1$s — ще го потърсим, щом каталогът се зареди.", "Saved %1$s — we’ll look it up once the catalogue loads."),
"scan_manual": ("Въведи номера на ръка", "Enter the number manually"),
"scan_manual_text": ("ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката.", "The ISBN (10 or 13 digits under the barcode) or the inventory number on the library label."),
"scan_manual_label": ("ISBN или инвентарен номер", "ISBN or inventory number"),
"scan_manual_invalid": ("Това не е ISBN или инвентарен номер — провери цифрите.", "That isn’t an ISBN or an inventory number — check the digits."),
"scan_manual_search": ("Търси", "Search"),
"scan_open_book": ("Отвори книгата", "Open book"),
"scan_not_found_title": ("Няма я в библиотеката", "Not in the library"),
"scan_not_found_text": ("Не намерихме книга с %1$s в каталога.", "We couldn’t find a book with %1$s in the catalogue."),
"scan_search_catalog": ("Търси в каталога", "Search the catalogue"),
"scan_code_isbn": ("ISBN %1$s", "ISBN %1$s"),
"scan_code_inv": ("инв. № %1$s", "inv. no. %1$s"),
"subject_membership": ("членството", "your membership"),
"subject_messages": ("съобщенията", "the messages"),
"subject_gallery": ("галерията", "the gallery"),
"subject_contacts": ("контактите", "the contacts"),
"subject_documents": ("документите", "the documents"),
"subject_page": ("страницата", "the page"),
# Езикът, на който реално е интерфейсът (Formatters.isBulgarian) — не се превежда, а показва кой файл е зареден.
"ui_language": ("bg", "en"),
# Разделите по УДК (UdcLabels) — от приложението, не от каталога.
"udc_0": ("Общ отдел", "General works"),
"udc_1": ("Философия. Психология", "Philosophy. Psychology"),
"udc_2": ("Религия", "Religion"),
"udc_3": ("Обществени науки", "Social sciences"),
"udc_5": ("Природни науки", "Natural sciences"),
"udc_6": ("Приложни науки. Техника", "Applied sciences. Technology"),
"udc_7": ("Изкуство. Спорт", "Arts. Sport"),
"udc_8": ("Език. Литература", "Language. Literature"),
"udc_9": ("География. История", "Geography. History"),
"udc_none": ("Без раздел", "No section"),
"book_genre_udc": ("%1$s (УДК %2$s)", "%1$s (UDC %2$s)"),
"na_title": ("Все още не е достъпно", "Not available yet"),
"na_login": ("Онлайн входът още не е включен от библиотеката. Когато бъде включен, функцията ще се появи тук автоматично. За достъп ще ти трябва ПИН, който се издава в библиотеката.", "Online sign-in hasn’t been switched on by the library yet. It will appear here automatically once it is. You will need a PIN issued at the library."),
"na_loans": ("Заетите книги и сроковете ще се показват тук, когато библиотечната система предостави онлайн достъп.", "Borrowed books and due dates will appear here once the library system offers online access."),
"na_membership": ("Данните за членството ще се показват тук, когато библиотечната система предостави онлайн достъп.", "Membership details will appear here once the library system offers online access."),
"na_holds": ("Заявяването на книги онлайн още не се поддържа. Попитай в библиотеката.", "Online holds aren’t supported yet. Please ask at the library."),
"na_renew": ("Онлайн удължаване още не се поддържа. Попитай в библиотеката.", "Online renewals aren’t supported yet. Please ask at the library."),
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
"home_for_you": ("За теб", "For you"),
"home_for_you_personal": ("По твоето четене", "Based on your reading"),
"home_for_you_popular": ("Най-четени и нови", "Popular and new"),
"home_latest_news": ("Още новини", "More news"),
"news_search_hint": ("Търси в новините", "Search news"),
"news_saved": ("Запазени", "Saved"),
"news_empty": ("Няма новини по този критерий.", "No news match this filter."),
"news_no_saved": ("Все още нямаш запазени новини.", "You have no saved news yet."),
"news_unsaved": ("Новината е премахната от запазените.", "Removed from saved news."),
"article_gallery": ("Галерия", "Gallery"),
"photo_n": ("Снимка %1$d", "Photo %1$d"),
"photo_n_of_m": ("Снимка %1$d от %2$d", "Photo %1$d of %2$d"),
"photo_in": ("%1$s – %2$s", "%1$s – %2$s"),
"photo_unavailable": ("Снимката липсва", "Photo unavailable"),
"photo_unavailable_named": ("%1$s — снимката липсва", "%1$s — photo unavailable"),
"photo_not_loaded": ("Не се зареди", "Didn’t load"),
"photo_not_loaded_named": ("%1$s — снимката не се зареди", "%1$s — photo didn’t load"),
"photo_missing_on_site": ("Снимката липсва на сайта.", "This photo is missing from the website."),
"photo_load_failed": ("Снимката не се зареди — може да липсва на сайта или да няма връзка.", "The photo didn’t load — it may be missing from the website, or there’s no connection."),
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
"calendar_month_empty": ("Няма прояви в този месец.", "No events this month."),
"news_events_title": ("Новини и събития", "News and events"),
"news_segment_news": ("Новини", "News"),
"news_segment_calendar": ("Календар", "Calendar"),
"event_open_map": ("покажи мястото на картата", "show the place on the map"),
"ics_subscribe": ("Абонирай се за календара", "Subscribe to the calendar"),
"ics_subscribe_action": ("Абонирай се", "Subscribe"),
"ics_sheet_text": ("Добави събитията на читалището в календара на телефона си. Новите прояви ще се появяват там сами.", "Add the centre’s events to your phone’s calendar. New events will appear there on their own."),
"ics_google_hint": ("Google Календар на Android не може да се абонира направо. Копирай връзката и я добави от calendar.google.com в „Други календари › От URL адрес“ — после календарът се вижда и на телефона.", "Google Calendar on Android can’t subscribe directly. Copy the link and add it at calendar.google.com under “Other calendars › From URL” — the calendar then shows on your phone too."),
"ics_copy_link": ("Копирай връзката", "Copy link"),
"ics_link_label": ("Календар на читалището", "Community centre calendar"),
"catalog_sort": ("Подреждане", "Sort"),
"catalog_filters": ("Филтри", "Filters"),
"catalog_hint_all": ("Търси навсякъде", "Search everything"),
"catalog_hint_title": ("Търси по заглавие", "Search by title"),
"catalog_hint_author": ("Търси по автор", "Search by author"),
"catalog_hint_isbn": ("Търси по ISBN", "Search by ISBN"),
"catalog_hint_keyword": ("Търси по ключова дума", "Search by keyword"),
"catalog_hint_inventory": ("Търси по инвентарен номер", "Search by inventory number"),
"catalog_no_results": ("Не открихме книги по това търсене.", "No books match this search."),
"catalog_no_results_hint": ("Опитай с друга дума, провери правописа или премахни филтрите.", "Try another word, check the spelling or remove filters."),
"catalog_data_as_of": ("данни от InvLib към %1$s", "InvLib data as of %1$s"),
"catalog_searching": ("Търсим…", "Searching…"),
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
"share_book_footer": ("В електронния каталог: %1$s", "In the online catalogue: %1$s"),
"book_ask_library": ("Попитай библиотеката", "Ask the library"),
"book_ask_text": ("Книгата сега не е на рафта. Обади се или пиши на библиотеката, за да разбереш кога ще е налична.", "This book isn’t on the shelf right now. Call or email the library to find out when it will be available."),
"book_ask_subject": ("Книга: %1$s", "Book: %1$s"),
"book_ask_body": ("Здравейте,\n\nИскам да попитам за книгата:\n\nЗаглавие: %1$s\nАвтор: %2$s\nСигнатура: %3$s\nИнвентарен номер: %4$s\n\n", "Hello,\n\nI’d like to ask about this book:\n\nTitle: %1$s\nAuthor: %2$s\nCall number: %3$s\nInventory number: %4$s\n\n"),
"book_search_author": ("Търси книги от автора", "Find books by this author"),
"action_copy": ("Копирай", "Copy"),
"copied": ("Копирано: %1$s", "Copied: %1$s"),
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
"want_title": ("Искам да прочета", "Want to read"),
"my_want_desc": ("Книги, които искаш да вземеш", "Books you’d like to borrow"),
"want_added": ("Добавена в „Искам да прочета“", "Added to “Want to read”"),
"want_removed": ("„%1$s“ е премахната от списъка", "“%1$s” removed from your list"),
"want_undo": ("Отмени", "Undo"),
"want_remove": ("Премахни от списъка", "Remove from list"),
"want_more": ("Още действия", "More actions"),
"want_notify": ("Кажи ми, когато е на рафта", "Notify me when it’s on the shelf"),
"want_notify_on": ("Ще ти кажем, когато е на рафта", "We’ll notify you when it’s on the shelf"),
"want_notify_stop": ("Не ми казвай", "Stop notifying me"),
"want_notify_set": ("Ще получиш известие, когато книгата е на рафта.", "You’ll get a notification when the book is on the shelf."),
"want_notify_unset": ("Няма да получаваш известие за тази книга.", "You won’t be notified about this book."),
"want_notify_blocked": ("Известията са изключени — няма как да ти кажем, когато книгата е на рафта.", "Notifications are off — we can’t tell you when the book is on the shelf."),
"want_library_off": ("Известията „Отново на рафта“ са изключени в приложението.", "“Back on the shelf” notifications are turned off in the app."),
"want_library_turn_on": ("Включи", "Turn on"),
"want_missing": ("Няма я в каталога", "Not in the catalogue"),
"want_empty": ("Списъкът е празен", "Your list is empty"),
"want_empty_hint": ("Отбележи книга с „Искам да прочета“ на страницата ѝ в каталога — тук ще виждаш дали е на рафта.", "Mark a book with “Want to read” on its catalogue page — here you’ll see whether it’s on the shelf."),
"want_clear": ("Изчисти списъка", "Clear list"),
"want_clear_confirm": ("Всички книги от „Искам да прочета“ ще бъдат премахнати от това устройство.", "All books in “Want to read” will be removed from this device."),
"want_cleared": ("Списъкът е изчистен.", "List cleared."),
"want_hint_swipe": ("Плъзни книга настрани, за да я премахнеш.", "Swipe a book aside to remove it."),
"notif_want_available": ("„%1$s“ вече е на рафта", "“%1$s” is now on the shelf"),
"notif_want_available_text": ("От списъка ти „Искам да прочета“ · по последния каталог на библиотеката", "From your “Want to read” list · according to the library’s latest catalogue"),
"notif_public_want": ("Книга от списъка ти е на рафта", "A book from your list is on the shelf"),
"my_membership": ("Моето членство", "My membership"),
"my_membership_desc": ("Статус и валидност", "Status and validity"),
"my_profile": ("Моят профил", "My profile"),
"my_profile_desc": ("Читателски данни", "Reader details"),
"my_notifications": ("Известия", "Notifications"),
"my_notifications_desc": ("Срокове, събития, новини", "Due dates, events, news"),
"guest_title": ("Здравей!", "Welcome!"),
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
"login_forgot": ("Забравен ПИН?", "Forgot your PIN?"),
"login_reset_sent": ("Ако номерът е регистриран, ще получиш инструкции.", "If the number is registered, you’ll receive instructions."),
"login_wrong": ("Грешен читателски номер или ПИН.", "Wrong reader number or PIN."),
"login_caps_failed": ("Няма връзка със сървъра на библиотеката, затова не можем да проверим дали онлайн входът е включен. Провери интернет връзката и опитай отново.", "We can’t reach the library’s server, so we can’t check whether online sign-in is available. Check your internet connection and try again."),
"login_caps_failed_short": ("Не можем да проверим дали онлайн входът е включен.", "We can’t check whether online sign-in is available."),
"login_privacy_note": ("ПИН-ът не се съхранява на устройството. Сесията се пази шифрована с ключ от Android Keystore.", "Your PIN is never stored on the device. The session is kept encrypted with an Android Keystore key."),
"login_demo_hint": ("Демо вход: DEMO-0001 / demo", "Demo sign-in: DEMO-0001 / demo"),
"card_add_manual": ("Добави читателска карта", "Add library card"),
"card_add_title": ("Дигитална карта", "Digital card"),
"card_add_text": ("Въведи номера от своята читателска карта. Приложението ще покаже баркод в същия формат (Code 39), в който библиотеката печата картите.", "Enter the number from your library card. The app shows a barcode in the same format (Code 39) the library prints on cards."),
"card_number_label": ("Номер на картата", "Card number"),
"card_number_hint": ("Главни латински букви, цифри и „-“", "Capital Latin letters, digits and “-”"),
"card_number_invalid": ("Номерът съдържа неразрешени знаци.", "The number contains invalid characters."),
"card_name_label": ("Име (по желание)", "Name (optional)"),
"card_show": ("Покажи картата", "Show card"),
"card_self_note": ("Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устройство.", "You entered this number; it hasn’t been verified by the library. It is stored encrypted on this device only."),
"card_usage_note": ("Покажи картата на библиотекаря; баркодът може да се сканира от четеца на гишето.", "Show the card to the librarian; the barcode can be scanned at the desk."),
"card_remove_title": ("Да премахнем ли картата от устройството?", "Remove the card from this device?"),
"card_login_instead": ("Вход с читателски данни", "Sign in as a reader"),
"card_library_line": ("ЧИТАТЕЛСКА КАРТА", "LIBRARY CARD"),
"card_barcode_desc": ("Баркод на карта %1$s", "Barcode for card %1$s"),
"card_qr_desc": ("QR код на картата", "Card QR code"),
"card_flip": ("Обърни картата", "Flip the card"),
"card_flip_hint": ("Докосни картата, за да видиш номера едро", "Tap the card to see the number in large print"),
"card_tile_add": ("Добави в бързите настройки", "Add to Quick Settings"),
"card_tile_dismiss": ("Не предлагай отново", "Don’t suggest again"),
"tile_card_label": ("Читателска карта", "Library card"),
"tile_card_subtitle": ("Покажи на гишето", "Show at the desk"),
"tile_card_subtitle_add": ("Добави картата", "Add your card"),
"widget_label": ("Читалище Яворец", "Chitalishte Yavorets"),
"widget_label_short": ("Читалище", "Chitalishte"),
"widget_description": ("Срокове за връщане, следващо събитие и бързо сканиране", "Due dates, the next event and a quick scan"),
"widget_next_event": ("Следващо събитие", "Next event"),
"widget_upcoming_events": ("Предстоящи събития", "Upcoming events"),
"widget_no_events": ("Няма предстоящи събития", "No upcoming events"),
"widget_next_due": ("Следващ срок", "Next due"),
"widget_due_today": ("днес", "today"),
"widget_due_tomorrow": ("утре", "tomorrow"),
"widget_no_due_date": ("без срок", "no due date"),
"widget_preview_due": ("след 3 дни", "in 3 days"),
"widget_preview_books": ("2 книги за връщане", "2 books to return"),
"widget_preview_date": ("15.10", "Oct 15"),
"widget_preview_event": ("Литературна вечер", "Literary evening"),
"widget_preview_event_date": ("18 октомври, 18:00", "October 18, 18:00"),
"shortcut_catalog": ("Каталог", "Catalogue"),
"shortcut_card": ("Читателска карта", "Library card"),
"shortcut_scan": ("Сканирай", "Scan"),
"shortcut_catalog_long": ("Търсене в каталога", "Search the catalogue"),
"shortcut_card_long": ("Моята читателска карта", "My library card"),
"shortcut_scan_long": ("Сканирай баркод на книга", "Scan a book barcode"),
"loans_empty": ("Нямаш заети книги", "You have no borrowed books"),
"loans_empty_hint": ("Разгледай каталога — може би нещо ще те заинтригува.", "Browse the catalogue — something might catch your eye."),
"loan_borrowed": ("Взета на %1$s", "Borrowed on %1$s"),
"loan_due": ("Връщане: %1$s", "Due: %1$s"),
"loan_renew": ("Удължи срока", "Renew"),
"loan_stale_note": ("Последно синхронизирани данни — може да не са актуални.", "Last synced data — may be out of date."),
"loan_cannot_renew": ("Не може да се удължи онлайн — попитай в библиотеката", "Can’t be renewed online — please ask at the library"),
"loan_due_overdue": ("Срокът изтече на %1$s", "Was due on %1$s"),
"loan_renewing": ("Изпраща се заявка за удължаване", "Sending the renewal request"),
"loan_open_book": ("Отвори книгата в каталога", "Open the book in the catalogue"),
"home_loans_label_next": ("Следващ срок", "Next due"),
"home_loans_label_overdue": ("Просрочено", "Overdue"),
"home_loans_return_by": ("Върни до %1$s: %2$s", "Return by %1$s: %2$s"),
"home_loans_open": ("Отвори „Моите книги“", "Open “My books”"),
"my_books_next_due": ("Следващ срок: %1$s", "Next due: %1$s"),
"wait_about": ("около %1$s", "about %1$s"),
"error_rate_limited_wait": ("Твърде много опити. Опитай отново след %1$s.", "Too many attempts. Try again in %1$s."),
"login_locked": ("Входът е временно заключен. Опитай отново след %1$s.", "Sign-in is temporarily locked. Try again in %1$s."),
"login_forgot_title": ("Забравен ПИН", "Forgot your PIN"),
"login_forgot_no_contacts": ("Телефона и имейла на библиотеката ще намериш в „Още › Контакти“.", "You’ll find the library’s phone and email under “More › Contacts”."),
"book_status_now": ("Наличност в момента", "Availability right now"),
"renew_ok": ("Удължено до %1$s.", "Renewed until %1$s."),
"membership_number": ("Членски номер", "Member number"),
"membership_since": ("Член от", "Member since"),
"membership_valid_until": ("Валидно до", "Valid until"),
"profile_photo": ("Профилна снимка", "Profile photo"),
"profile_reader_number": ("Читателски номер", "Reader number"),
"profile_category": ("Категория", "Category"),
"profile_email": ("Имейл", "Email"),
"profile_registered": ("Дата на регистрация", "Registration date"),
"profile_edit_note": ("Личните данни се поддържат от библиотеката. За промяна се обърни към библиотекаря.", "Personal details are managed by the library. Please ask the librarian to change them."),
"profile_delete_request": ("Искане за изтриване на данните", "Request data deletion"),
"profile_delete_text": ("Библиотеката ще получи искане да изтрие читателските ти данни съгласно ОРЗД. Заетите книги трябва да бъдат върнати.", "The library will receive a request to delete your reader data under the GDPR. Borrowed items must be returned first."),
"account_delete_web": ("Изтриване на акаунта и данните", "Delete account and data"),
"account_delete_web_desc": ("Искане за изтриване на читателския акаунт и свързаните с него данни (chyavorec.org)", "Request deletion of your reader account and its data (chyavorec.org)"),
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
"contacts_map": ("Карта", "Map"),
"contacts_site": ("Отвори сайта", "Open website"),
"contacts_site_short": ("Сайт", "Website"),
"contacts_call_cd": ("Обади се на читалището", "Call the centre"),
"contacts_email_cd": ("Изпрати имейл на читалището", "Email the centre"),
"contacts_map_cd": ("Покажи читалището на картата", "Show the centre on the map"),
"contacts_site_cd": ("Отвори сайта на читалището", "Open the centre’s website"),
"contacts_address": ("Адрес", "Address"),
"contacts_see_site": ("Виж страницата „Контакти“ в сайта", "See the Contacts page on the website"),
"contacts_phone": ("Телефон", "Phone"),
"contacts_email_label": ("Имейл", "Email"),
"contacts_hours": ("Работно време", "Opening hours"),
"contacts_hours_unknown": ("Не е обявено в сайта", "Not listed on the website"),
"contacts_web": ("В интернет", "Online"),
"contacts_fallback_hours": ("Понеделник – четвъртък: 08:00 – 16:30\nПетък: 08:00 – 16:00\nСъбота и неделя: почивни дни", "Monday – Thursday: 08:00 – 16:30\nFriday: 08:00 – 16:00\nSaturday and Sunday: closed"),
"contacts_fallback_note": ("Контактите от сайта не се заредиха — показваме основните данни, вградени в приложението. Може да не са актуални.", "Contacts couldn’t be loaded from the website — showing the basic details built into the app. They may be out of date."),
"contacts_source": ("Данни от chyavorec.org", "Data from chyavorec.org"),
"more_events_desc": ("Календар на читалището", "Community centre calendar"),
"more_activities_desc": ("Групи, клубове, проекти", "Groups, clubs, projects"),
"more_about_desc": ("История и мисия", "History and mission"),
"more_digital_desc": ("Услуги, обучения, работно време", "Services, training, hours"),
"more_gallery_desc": ("Снимки от сайта", "Photos from the website"),
"more_contacts_desc": ("Адрес, телефон, местоположение", "Address, phone, location"),
"search_hint": ("Книги, автори, новини, събития…", "Books, authors, news, events…"),
"search_books": ("Книги", "Books"),
"search_authors": ("Автори", "Authors"),
"search_pages": ("Страници", "Pages"),
"search_show_all": ("Покажи всички (%1$d)", "Show all (%1$d)"),
"search_recent": ("Скорошни търсения", "Recent searches"),
"search_recent_cleared": ("Скорошните търсения са изчистени.", "Recent searches cleared."),
"search_start": ("Какво търсиш?", "What are you looking for?"),
"search_start_hint": ("Търсенето обхваща каталога, новините, събитията и страниците на читалището.", "Search covers the catalogue, news, events and the centre’s pages."),
"search_no_results": ("Нищо не открихме", "No results"),
"search_no_results_hint": ("Опитай с друга дума или по-кратко търсене.", "Try another word or a shorter search."),
"settings_title": ("Настройки", "Settings"),
"settings_appearance": ("Изглед", "Appearance"),
"settings_theme": ("Тема", "Theme"),
"settings_dynamic": ("Цветове от тапета", "Colours from wallpaper"),
"settings_dynamic_desc": ("Цветовете на телефона, взети от тапета, вместо тези на читалището", "Material You colours from your wallpaper instead of the centre’s"),
"settings_language": ("Език", "Language"),
"settings_notifications": ("Известия", "Notifications"),
"settings_notifications_desc": ("Заемания, събития, съобщения и новини — кои да получаваш", "Loans, events, messages and news — which ones you get"),
"settings_notifications_choose": ("Избери известията", "Choose notifications"),
"settings_text_size": ("Размер на текста", "Text size"),
"settings_text_size_note": ("Умножава размера на шрифта от настройките на телефона. В статиите бутонът „Аа“ го променя допълнително.", "Multiplies the font size set on your phone. In articles, the “Aa” button adjusts it further."),
"text_size_small": ("Малък", "Small"),
"text_size_standard": ("Стандартен", "Standard"),
"text_size_large": ("Голям", "Large"),
"text_size_xlarge": ("Много голям", "Extra large"),
"settings_data": ("Данни", "Data"),
"settings_clear_cache": ("Изчисти временните данни", "Clear temporary data"),
"settings_covers_online": ("Корици от интернет", "Covers from the internet"),
"settings_covers_online_desc": ("Корици по ISBN от covers.openlibrary.org и сканираните корици от каталога (сървърите виждат IP адреса). В личните екрани — никога", "Covers by ISBN from covers.openlibrary.org and scanned covers from the catalogue (the servers see your IP address). Never on personal screens"),
"settings_covers_wifi_only": ("Корици само по Wi-Fi", "Covers on Wi-Fi only"),
"settings_covers_wifi_only_desc": ("По мобилни данни — само вече заредените корици, без нови заявки", "On mobile data, only covers already loaded — no new downloads"),
"settings_seasonal": ("Сезонни украси", "Seasonal decorations"),
"settings_seasonal_desc": ("Празници, сезони и обичаи: поздрави, орнаменти, сняг и листа", "Holidays, seasons and traditions: greetings, ornaments, snow and leaves"),
"occ_spring_title": ("Честита пролет!", "Happy spring!"),
"occ_spring_text": ("Днес е пролетното равноденствие — денят и нощта са равни.", "Today is the spring equinox — day and night are equal."),
"occ_summer_title": ("Лятно слънцестоене", "Summer solstice"),
"occ_summer_text": ("Най-дългият ден в годината.", "The longest day of the year."),
"occ_autumn_title": ("Есенно равноденствие", "Autumn equinox"),
"occ_autumn_text": ("Започва астрономическата есен — време за книга и топъл чай.", "Astronomical autumn begins — time for a good book and a warm cup of tea."),
"occ_winter_title": ("Зимно слънцестоене", "Winter solstice"),
"occ_winter_text": ("Най-дългата нощ в годината. От утре денят расте.", "The longest night of the year. From tomorrow the days grow longer."),
"occ_new_year_title": ("Честита Нова година!", "Happy New Year!"),
"occ_new_year_text": ("Сурва, сурва година, весела година! Здраве и много хубави книги.", "Surva, surva godina — a merry year! Wishing you health and many good books."),
"occ_liberation_title": ("3 март – Ден на Освобождението", "3 March – Liberation Day"),
"occ_liberation_text": ("Национален празник на България. Честит празник!", "Bulgaria’s national holiday. Happy holiday!"),
"occ_labour_title": ("1 май – Ден на труда", "1 May – Labour Day"),
"occ_labour_text": ("Честит празник на всички, които работят!", "Happy holiday to all working people!"),
"occ_gergyovden_title": ("Честит Гергьовден!", "Happy St George’s Day!"),
"occ_gergyovden_text": ("Ден на храбростта и празник на Българската армия. Честит имен ден на всички, които празнуват!", "Day of Bravery and of the Bulgarian Army. Happy name day to everyone celebrating!"),
"occ_may24_title": ("24 май – Ден на светите Кирил и Методий", "24 May – Saints Cyril and Methodius Day"),
"occ_may24_text": ("Ден на българската азбука, просвета и култура и на славянската книжовност. Честит празник!", "Day of the Bulgarian alphabet, education and culture, and of Slavonic literature. Happy holiday!"),
"occ_unification_title": ("6 септември – Ден на Съединението", "6 September – Unification Day"),
"occ_unification_text": ("През 1885 г. Княжество България и Източна Румелия се обединяват. Честит празник!", "In 1885 the Principality of Bulgaria and Eastern Rumelia were united. Happy holiday!"),
"occ_independence_title": ("22 септември – Ден на Независимостта", "22 September – Independence Day"),
"occ_independence_text": ("През 1908 г. България е обявена за независима държава. Честит празник!", "In 1908 Bulgaria was proclaimed an independent state. Happy holiday!"),
"occ_good_friday_title": ("Разпети петък", "Good Friday"),
"occ_good_friday_text": ("Ден на тишина и помен.", "A day of silence and remembrance."),
"occ_easter_title": ("Христос воскресе!", "Christ is risen!"),
"occ_easter_text": ("Воистина воскресе! Честит Великден на всички!", "Truly He is risen! Happy Easter to everyone!"),
"occ_christmas_eve_title": ("Бъдни вечер", "Christmas Eve"),
"occ_christmas_eve_text": ("Нека трапезата е богата, а домът — топъл. Весели празници!", "May your table be rich and your home warm. Happy holidays!"),
"occ_christmas_title": ("Честито Рождество Христово!", "Merry Christmas!"),
"occ_christmas_text": ("Весела Коледа на всички от читалището!", "Merry Christmas to everyone from the community centre!"),
"occ_levski_memorial_title": ("Поклон пред Апостола", "In memory of the Apostle"),
"occ_levski_memorial_text": ("Днес почитаме паметта на Васил Левски, чието име носи нашето читалище.", "Today we honour the memory of Vasil Levski, whose name our community centre bears."),
"occ_levski_birth_title": ("Роден е Васил Левски", "Vasil Levski’s birthday"),
"occ_levski_birth_text": ("На 18 юли 1837 г. в Карлово е роден Апостолът на свободата — патронът на нашето читалище.", "The Apostle of Freedom, patron of our community centre, was born in Karlovo on 18 July 1837."),
"occ_botev_title": ("Ден на Ботев", "Botev Day"),
"occ_botev_text": ("Ден на Христо Ботев и на загиналите за свободата и независимостта на България. В 12 ч. — минута мълчание.", "Day of Hristo Botev and of those who died for Bulgaria’s freedom and independence. A minute of silence at noon."),
"occ_enlighteners_title": ("Ден на народните будители", "National Enlighteners’ Day"),
"occ_enlighteners_text": ("Почит към будителите — и към всички, които и днес пазят читалището живо. Честит празник!", "Honouring the enlighteners — and everyone who keeps our community centre alive today. Happy holiday!"),
"occ_children_book_title": ("Ден на детската книга", "International Children’s Book Day"),
"occ_children_book_text": ("Рожденият ден на Андерсен. Разгледай детските книги в библиотеката.", "Hans Christian Andersen’s birthday. Browse the children’s books in our library."),
"occ_book_day_title": ("Световен ден на книгата", "World Book Day"),
"occ_book_day_text": ("Отбележи го с нова книга — виж най-новите в библиотеката.", "Celebrate with a new book — see the latest additions to our library."),
"occ_children_day_title": ("Честит Ден на детето!", "Happy Children’s Day!"),
"occ_children_day_text": ("Усмивки, игри и книжки за всички деца!", "Smiles, games and books for all children!"),
"occ_jordan_title": ("Честит Йордановден!", "Happy Epiphany!"),
"occ_jordan_text": ("Богоявление. Честит имен ден на всички, които празнуват!", "Theophany. Happy name day to everyone celebrating!"),
"occ_babinden_title": ("Честит Бабинден!", "Happy Babinden!"),
"occ_babinden_text": ("Празник на бабите и на родилната помощ.", "The day of grandmothers and midwives."),
"occ_trifon_title": ("Трифон Зарезан", "Trifon Zarezan"),
"occ_trifon_text": ("Празник на лозаря — наздраве за богата реколта!", "The winegrowers’ feast — cheers to a rich harvest!"),
"occ_baba_marta_title": ("Честита Баба Марта!", "Happy Baba Marta!"),
"occ_baba_marta_text": ("Първа пролет! Закичи се с мартеница за здраве и късмет.", "The first day of spring! Wear a martenitsa for health and luck."),
"occ_lazarovden_title": ("Лазаровден", "Lazarovden"),
"occ_lazarovden_text": ("Лазарките пеят за здраве и плодородие. Честит имен ден!", "Girls sing for health and fertility. Happy name day!"),
"occ_tsvetnitsa_title": ("Честита Цветница!", "Happy Palm Sunday!"),
"occ_tsvetnitsa_text": ("Честит имен ден на всички с имена на цветя!", "Happy name day to everyone named after a flower!"),
"occ_forgiveness_title": ("Сирни заговезни", "Forgiveness Sunday"),
"occ_forgiveness_text": ("Прошка! — Простено да ти е. Утре започва Великият пост.", "Forgive me! — You are forgiven. Great Lent begins tomorrow."),
"occ_ascension_title": ("Спасовден", "Ascension Day"),
"occ_ascension_text": ("Възнесение Господне. Честит имен ден на всички, които празнуват!", "The Ascension of the Lord. Happy name day to everyone celebrating!"),
"occ_enyovden_title": ("Честит Еньовден!", "Happy Enyovden!"),
"occ_enyovden_text": ("Днес билките са най-лековити, а утринната роса носи здраве.", "Midsummer: herbs are most healing today and the morning dew brings health."),
"occ_ilinden_title": ("Илинден", "Ilinden"),
"occ_ilinden_text": ("Денят на свети Илия и годишнина от Илинденско-Преображенското въстание.", "St Elijah’s Day and the anniversary of the Ilinden–Preobrazhenie Uprising."),
"occ_assumption_title": ("Голяма Богородица", "Dormition of the Mother of God"),
"occ_assumption_text": ("Успение на Пресвета Богородица. Честит имен ден на всички, които празнуват!", "The Dormition of the Most Holy Mother of God. Happy name day to everyone celebrating!"),
"occ_dimitrovden_title": ("Честит Димитровден!", "Happy St Demetrius’ Day!"),
"occ_dimitrovden_text": ("Честит имен ден на всички, които празнуват!", "Happy name day to everyone celebrating!"),
"occ_nikulden_title": ("Честит Никулден!", "Happy St Nicholas’ Day!"),
"occ_nikulden_text": ("Ден на рибарите и моряците. Честит имен ден на всички, които празнуват!", "The day of fishermen and sailors. Happy name day to everyone celebrating!"),
"occ_ignazhden_title": ("Игнажден", "Ignazhden"),
"occ_ignazhden_text": ("Започват коледните празници. Нека първият гост донесе късмет!", "The Christmas season begins. May your first guest bring luck!"),
"occ_dismiss": ("Скрий поздрава", "Hide greeting"),
"occ_open_books": ("Към книгите", "See the books"),
"catalog_view_list": ("Списък", "List"),
"catalog_view_shelf": ("Полица", "Shelf"),
"reader_mode": ("Режим за четене", "Reading mode"),
"reader_font_size": ("Размер на шрифта", "Font size"),
"reader_mode_glyph": ("Аа", "Aa"),
"reader_size_s": ("Малък шрифт", "Small text"),
"reader_size_m": ("Нормален шрифт", "Normal text"),
"reader_size_l": ("Голям шрифт", "Large text"),
"reader_size_xl": ("Много голям шрифт", "Extra large text"),
"reader_width": ("Ширина на текста", "Text width"),
"reader_width_normal": ("Нормална", "Normal"),
"reader_width_narrow": ("Тясна", "Narrow"),
"settings_clear_cache_desc": ("Новините, събитията, каталогът, кориците и снимките, запазени за четене без интернет, и свалените документи. Свалят се наново при нужда", "News, events, catalogue, covers and images saved for reading offline, and downloaded documents. They’re downloaded again when needed"),
"settings_clear_cache_confirm": ("Временните данни (новини, събития, каталог, корици и снимки, запазени за четене без интернет, и свалените документи) ще бъдат изтрити и свалени отново при нужда. Входът, картата, запазените новини, „Искам да прочета“, напомнянията и историята остават.", "Temporary data (news, events, catalogue, covers and images saved for reading offline, and downloaded documents) will be deleted and downloaded again when needed. Your sign-in, card, saved news, “Want to read”, reminders and history stay."),
"settings_cache_cleared": ("Временните данни са изтрити.", "Temporary data cleared."),
"settings_legal": ("Информация", "Information"),
"theme_system": ("Както системата", "System default"),
"theme_light": ("Светла", "Light"),
"theme_dark": ("Тъмна", "Dark"),
"settings_palette": ("Палитра", "Palette"),
"palette_yavorets": ("Яворец", "Yavorets"),
"palette_yavorets_desc": ("Злато и жито", "Gold and wheat"),
"palette_levski": ("Левски", "Levski"),
"palette_levski_desc": ("Зелено и сребро", "Green and silver"),
"palette_shevitsa": ("Шевица", "Shevitsa"),
"palette_shevitsa_desc": ("Червено, черно и слонова кост", "Red, black and ivory"),
"palette_dynamic_note": ("Не се прилага, докато са включени цветовете от тапета", "Not applied while colours from wallpaper are on"),
"settings_oled": ("Черно (OLED)", "Black (OLED)"),
"settings_oled_desc": ("Чисто черен фон в тъмната тема — пести батерия на OLED екрани", "Pure black background in the dark theme — saves battery on OLED screens"),
"language_system": ("Както системата", "System default"),
"notifications_intro": ("Известията се подготвят на самото устройство — без рекламни или проследяващи услуги.", "Notifications are prepared on the device itself — no advertising or tracking services."),
"notifications_loans_na": ("Ще работи, когато библиотечната система предостави онлайн достъп.", "Will work once the library system offers online access."),
"notifications_library_desc": ("Книги от „Искам да прочета“, които отново са на рафта", "Books from “Want to read” that are back on the shelf"),
"notifications_system": ("Системни настройки за известия", "System notification settings"),
# Разрешение за известия: отказ, изключени в системата, обяснение при първото стартиране.
"notifications_open_settings": ("Отвори настройките", "Open settings"),
"notifications_blocked_app": ("Известията са изключени за приложението — нищо от изброеното долу няма да се показва.", "Notifications are turned off for the app — none of the ones below will be shown."),
"notifications_blocked_channels": ("Изключени в системните настройки: %1$s.", "Turned off in system settings: %1$s."),
"notifications_denied": ("Без разрешение за известия това известие няма да се показва.", "Without notification permission this notification won’t be shown."),
"notifications_rationale_title": ("Известия от читалището", "Notifications from the centre"),
"notifications_rationale_text": ("Искаш ли известия за сроковете на книгите, нови съобщения и събития? Можеш да ги промениш по всяко време от „Настройки › Известия“.", "Would you like notifications about book due dates, new messages and events? You can change this any time in “Settings › Notifications”."),
"notifications_rationale_yes": ("Да", "Yes"),
"notifications_rationale_later": ("Не сега", "Not now"),
"reminder_set_at": ("Ще получиш напомняне на %1$s.", "You’ll get a reminder on %1$s."),
"reminder_blocked": ("Известията за събития са изключени — напомнянето няма да се покаже.", "Event notifications are turned off — the reminder wouldn’t be shown."),
"notifications_push_note": ("Сървърни (push) известия ще бъдат добавени, когато читалището въведе сървър за тях. Дотогава приложението проверява за новини и срокове периодично.", "Server (push) notifications will be added once the centre sets up a server for them. Until then the app checks for news and due dates periodically."),
"channel_group_my_library": ("Моята библиотека", "My library"),
"channel_group_centre": ("Читалището", "The centre"),
"channel_group_app": ("Приложението", "The app"),
"channel_loans": ("Срокове за връщане", "Due dates"),
"channel_loans_desc": ("Наближаващ или изтекъл срок на заета книга", "An upcoming or passed due date for a borrowed book"),
"channel_events": ("Събития", "Events"),
"channel_events_desc": ("Напомняния за събития", "Event reminders"),
"channel_news": ("Новини", "News"),
"channel_news_desc": ("Нова публикация в сайта", "A new post on the website"),
"channel_library": ("Отново на рафта", "Back on the shelf"),
"channel_library_desc": ("Книги от „Искам да прочета“, които отново са на рафта", "Books from “Want to read” that are back on the shelf"),
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
"whats_new_title": ("Какво ново във версия %1$s", "What’s new in version %1$s"),
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
"notif_updated": ("Обновено до версия %1$s", "Updated to version %1$s"),
"notif_updated_text": ("Докосни, за да отвориш приложението.", "Tap to open the app."),
"channel_messages": ("Съобщения", "Messages"),
"channel_messages_desc": ("Съобщения, изпратени от читалището до потребителите на приложението", "Messages the centre sends to app users"),
"channel_messages_important": ("Важни съобщения", "Important messages"),
"channel_messages_important_desc": ("Съобщения, отбелязани от читалището като важни", "Messages the centre marks as important"),
"messages_title": ("Съобщения", "Messages"),
"messages_empty": ("Няма съобщения от читалището", "No messages from the centre"),
"messages_members_hint": ("Някои съобщения са само за членове. Въведи читателската си карта (или влез) в „Моето“, за да ги виждаш.", "Some messages are for members only. Enter your library card (or sign in) under “My library” to see them."),
"messages_members_only": ("Само за членове", "Members only"),
"messages_new": ("Ново", "New"),
"messages_valid_until": ("Валидно до %1$s", "Valid until %1$s"),
"messages_open_link": ("Отвори връзката", "Open link"),
"messages_personal": ("Лично", "Personal"),
"messages_personal_title": ("Лично съобщение от библиотеката", "Personal message from the library"),
"messages_personal_notification_title": ("Лично съобщение от библиотеката", "Personal message from the library"),
"more_messages_desc": ("Съобщения и покани от читалището", "Announcements and invitations from the centre"),
"notif_new_article": ("Нова публикация", "New post"),
"notif_due_soon": ("Наближава срок за връщане", "Due date approaching"),
"notif_due_today": ("Днес е срокът за връщане", "Due back today"),
"notif_due_on": ("%1$s — до %2$s", "%1$s — due %2$s"),
"notif_overdue": ("Просрочена книга", "Overdue book"),
"notif_public_loan": ("Наближава срок за връщане на книга", "A library book is due soon"),
"notif_public_overdue": ("Изтекъл е срокът за връщане на книга", "A library book is overdue"),
"notif_public_message": ("Имаш лично съобщение от библиотеката", "You have a personal message from the library"),
"notif_public_renew": ("Отговор за удължаване на книга", "Update on a book renewal"),
"notif_loans_summary": ("Известия за заетите книги", "About your borrowed books"),
"notif_action_renew": ("Удължи", "Renew"),
"notif_action_route": ("Маршрут", "Directions"),
"notif_renew_sending": ("Изпраща се заявка за удължаване…", "Sending the renewal request…"),
"notif_renew_sent": ("Заявката за удължаване е изпратена", "Renewal request sent"),
"notif_renew_sent_text": ("„%1$s“: библиотеката ще удължи срока при следващото обновяване на програмата си.", "“%1$s”: the library will extend the due date at its program’s next update."),
"notif_renew_failed": ("Удължаването не е заявено", "Renewal not requested"),
"notif_renew_failed_text": ("„%1$s“: %2$s", "“%1$s”: %2$s"),
"notif_renew_session_unavailable": ("Профилът ти временно не се отваря на това устройство. Опитай пак по-късно.", "Your account can’t be opened on this device right now. Please try again later."),
"notif_renew_done_title": ("Срокът е удължен", "Due date extended"),
"notif_renew_done": ("Срокът на „%1$s“ е удължен до %2$s", "“%1$s” is now due back on %2$s"),
"notif_renew_rejected_title": ("Удължаването е отказано", "Renewal declined"),
"notif_renew_rejected": ("Удължаването на „%1$s“ е отказано: %2$s", "Renewal of “%1$s” was declined: %2$s"),
"notif_renew_rejected_noreason": ("Удължаването на „%1$s“ е отказано.", "Renewal of “%1$s” was declined."),
"notif_event_reminder": ("Напомняне за събитие", "Event reminder"),
"onb_skip": ("Пропусни", "Skip"),
"onb_next": ("Напред", "Next"),
"onb_login": ("Вход с читателски данни", "Sign in as a reader"),
"onb_guest": ("Продължи без вход", "Continue as guest"),
"onb_continue": ("Продължи", "Continue"),
"action_add_card": ("Добави читателска карта", "Add library card"),
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
"event_recurring": ("Събитие от годишния календар на читалището — повтаря се всяка година на тази дата.", "An event from the centre’s annual calendar — it recurs every year on this date."),
"contacts_persons": ("Лица за контакт", "Contact persons"),
"documents_title": ("Документи и архив", "Documents & archive"),
"documents_tab_docs": ("Документи", "Documents"),
"documents_tab_publications": ("Исторически публикации", "Historical articles"),
"documents_empty": ("Няма документи за показване.", "No documents to show."),
"pdf_open_with_app": ("Отвори с приложение за PDF", "Open with a PDF app"),
"pdf_downloading": ("Сваля се…", "Downloading…"),
"pdf_cancel_download": ("Откажи свалянето", "Cancel download"),
"pdf_download_failed": ("Файлът не се свали — отваряме го в браузъра", "Couldn’t download the file — opening it in the browser"),
"more_documents_desc": ("Устав, отчети, юбилейният вестник", "Statute, reports, jubilee newspaper"),
"catalog_title": ("Каталог", "Catalogue"),
"about_invlib_desc": ("Безплатната библиотечна система с отворен код, с която работи библиотеката. Оттук идва каталогът.", "The free, open-source library system the library uses. The catalogue comes from it."),
"about_catalog_desc": ("Книгите на библиотеката с актуална наличност — търсене по заглавие, автор, ключова дума.", "The library’s books with current availability — search by title, author, keyword."),
"about_creator": ("Създател", "Created by"),
"privacy_title": ("Политика за поверителност", "Privacy policy"),
"terms_title": ("Условия за използване", "Terms of use"),
"legal_site_version": ("Виж и: %1$s", "See also: %1$s"),
"about_title": ("За приложението", "About the app"),
"settings_diagnostics": ("Диагностика", "Diagnostics"),
"settings_diagnostics_desc": ("Дневник на грешките — само на това устройство", "Error log — kept on this device only"),
"diagnostics_intro": ("Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш.", "The app records technical errors here (time, place in the code and type of error) so they can be found and fixed. No personal data: numbers, PINs, names and addresses are removed. The log stays on this device only — it leaves it only if you choose to share it."),
"diagnostics_empty": ("Няма записани грешки.", "No errors recorded."),
"diagnostics_count": ("Записи: %1$d", "Entries: %1$d"),
"diagnostics_cleared": ("Дневникът е изчистен", "The log has been cleared"),
"diagnostics_share_subject": ("Диагностика — Читалище Яворец", "Diagnostics — Chitalishte Yavorets"),
"about_version": ("Версия %1$s", "Version %1$s"),
"about_version_full": ("Версия %1$s (%2$d)", "Version %1$s (%2$d)"),
"about_env": ("Среда: %1$s · демо данни: %2$s", "Environment: %1$s · demo data: %2$s"),
"about_invlib": ("Библиотечната система InvLib", "The InvLib library system"),
}

PRIVACY_BG = """Последна актуализация: 31 октомври 2026 г.

# Кой обработва данните

Приложението „Читалище Яворец“ е официалното мобилно приложение на Народно читалище „Васил Левски – 1922“, с. Яворец, общ. Габрово (наричано по-долу „читалището“). Връзка: chitalishte_yavorec@abv.bg.

# Принцип

Приложението събира възможно най-малко данни. То няма реклами, няма анализ на поведението (analytics), няма услуги за проследяване и не изпраща отчети за сривове към трети страни.

# Какви данни се обработват

Публично съдържание: новините, събитията, снимките и страниците се зареждат от сайта chyavorec.org, а библиотечният каталог — от публичния файл katalog.json на системата InvLib, хостван в GitHub (raw.githubusercontent.com и cdn.jsdelivr.net). При всяка такава заявка съответният сървър вижда IP адреса на устройството, както при посещение на обикновен уебсайт. Каталогът не съдържа лични данни на читатели. Кориците на книгите в публичния каталог се зареждат по ISBN от covers.openlibrary.org (Internet Archive) — този сървър вижда IP адреса и ISBN-а, нищо друго; ако в каталога е посочена сканирана корица, тя се зарежда от адреса, записан в каталога, и съответният сървър вижда IP адреса. И двете се изключват от „Настройки › Данни › Корици от интернет“. В личните екрани (история на четенето, заети книги, „Искам да прочета“, „За теб“, картата, както и страницата на книга, отворена от тях) корици от интернет не се зареждат никога — там кориците се рисуват на телефона.

Съобщения от читалището: приложението проверява публичния файл със съобщения на chyavorec.org най-често веднъж на час (по-рядко — до веднъж на 6 часа, докато няма нови съобщения) и спира проверките, ако не е отваряно 14 дни. Съобщенията „само за членове“ се показват, ако на устройството е въведена читателска карта или след вход — тази проверка е на устройството и нищо не се изпраща към сайта. Прочетените съобщения се помнят само на устройството.

Лични съобщения от библиотеката: след вход, ако сървърът на читалището го поддържа, библиотеката може да ти изпрати лично съобщение (напр. за запазена книга). То идва по HTTPS от сървъра на читалището, пази се шифровано на телефона и се изтрива при изход; отбелязването „прочетено“ се изпраща към същия сървър. Известието за ново лично съобщение се подготвя на телефона; когато заключеният екран е настроен да скрива чувствително съдържание, там то показва само „Имаш лично съобщение от библиотеката“, без съдържанието.

Обновления: версията, инсталирана извън Google Play, проверява за нова версия във файла update.json в GitHub (github.com) и изтегля новата версия оттам. Изпраща се само обикновена заявка (IP адресът е видим за GitHub); не се изпращат лични данни. Автоматичното обновяване се изключва от „Настройки › Обновления“. Версията от Google Play се обновява от Google Play.

Данни само на устройството: настройки (тема, език, известия), запазени новини, напомняния за събития, история на търсенията и кеширано публично съдържание за офлайн работа. Те не напускат устройството и не се архивират в облака. Последната книга, отворена от публичния каталог, става пряк път към нея при задържане на иконата на приложението; той също е само на телефона и се маха при „Изход“ и „Изчисти временните данни“. Приложението пази и локален дневник на грешките само на устройството, без лични данни; той напуска устройството единствено ако го споделиш ти („Настройки › Информация › Диагностика“).

„Искам да прочета“: списъкът с отбелязаните книги (и дали да ти кажем, когато някоя е на рафта) се пази само на устройството — наличността се сравнява с публичния каталог на телефона, без да се изпраща кои книги са отбелязани; изходът не го изтрива, а „Изчисти списъка“ (в самия списък) и деинсталирането — да. Ако за някоя книга е включено „Кажи ми, когато е на рафта“, наличността се проверява и във фонов режим: около веднъж на 6 часа приложението тегли публичния каталог katalog.json от GitHub (raw.githubusercontent.com или cdn.jsdelivr.net), без да изпраща списъка (само при включени известия „Отново на рафта“ и докато приложението е отваряно в последните 14 дни).

Читателска карта: ако въведеш номера на своята карта, той (и името, ако го въведеш) се пази шифрован с ключ от Android Keystore само на това устройство.

Вход (само при съгласие, дадено в библиотеката, и издаден ПИН): номерът на читателската карта и ПИН-ът се изпращат само по HTTPS към сървъра на читалището (chyavorec.org) и ПИН-ът никога не се записва. При вход се изпраща и моделът на телефона (напр. „Pixel 8“), за да се разпознава сесията. Пази се само шифрован токен за сесията. Името ти, заетите книги, сроковете и членството идват от този сървър, който получава от библиотечната програма само данните на съгласилите се читатели (без ЕГН, адрес и телефон), и се кешират шифровано на устройството. Съгласието се оттегля в библиотеката — тогава данните ти се изтриват от сървъра при следващото обновяване.

Мениджър на пароли: запазването на номера на картата и ПИН-а в мениджъра на пароли на телефона е по желание — прави се само ако се съгласиш, и те се пазят от системния мениджър на пароли, а не от приложението. Ако мениджърът на пароли (напр. Google Password Manager) синхронизира паролите, той може да синхронизира номера и ПИН-а и с профила ти в Google (или при съответния доставчик) — по неговите условия.

Защита на входа: след няколко неуспешни опита за вход входът временно се блокира (на устройството и на сървъра), за да не може ПИН-ът да бъде отгатнат.

Уиджет на началния екран: ако го добавиш, той показва само броя книги за връщане и сроковете им — без заглавия и без име.\n\nСкенер на баркодове: сканира скенерът на Google Play услугите — той включва камерата само докато е отворен и разчита кода на телефона; приложението няма достъп до камерата и получава само разчетения номер, а кадрите не се записват и не се изпращат от приложението.\n\nИстория на четенето: за читатели, дали съгласие в библиотеката, списъкът с върнати книги идва от сървъра на читалището и се пази шифрован на телефона. „Подобни книги“ се подбират само на телефона.\n\nУдължаване на срока: заявката се изпраща до сървъра на читалището и се изпълнява от библиотечната програма при следващото ѝ обновяване.

Заявка за книга: ако заявиш книга от каталога, заявката (коя книга) се изпраща до сървъра на читалището, за да може библиотеката да я отдели за теб.

Документи: „Отвори с приложение за PDF“ сваля документа от chyavorec.org във временните данни на приложението (пази се до седмица) и го отваря в избрано от теб приложение за PDF на телефона.

Абонамент за календара: ако добавиш календара на читалището (https://chyavorec.org/events.ics) в приложение за календар, то или неговата услуга (напр. Google Календар) тегли файла от сайта по свой график — като обикновена заявка, без лични данни; приложението на читалището не участва в това.

# Известия

Известията се подготвят на устройството (периодична проверка за нови публикации, съобщения, срокове и наличност на книгите от „Искам да прочета“). Не се използват сървъри за push известия. Когато заключеният екран е настроен да скрива чувствително съдържание, личните известия (за срокове на заети книги, лични съобщения и книги от „Искам да прочета“, върнати на рафта) показват там само общ текст (напр. „Наближава срок за връщане на книга“) — без заглавия и съдържание. Тази настройка е в системните настройки на телефона (известия на заключен екран); при показване на цялото съдържание се виждат и заглавията.

# Твоите права

Можеш по всяко време да изтриеш данните на устройството: „Изход“ изтрива сесията, читателските данни, известията за заемания и лични съобщения, напомнянията за срокове и личните изображения в кеша — снимката в профила и кориците на заетите книги (без „Запомни ме на това устройство“ същото става и при следващото стартиране, ако приложението е било затворено); „Премахни“ изтрива въведената карта; „Изчисти временните данни“ изтрива публичното съдържание и свалените документи; деинсталирането премахва всичко. Изтриване на профила за приложението и данните ти от сървъра можеш да поискаш на https://chyavorec.org/app-delete, в читалището или по имейл. За данните, които библиотеката поддържа за теб като читател (по Наредба № 3 и ОРЗД), можеш да поискаш достъп, корекция или изтриване в читалището или по имейл. Имаш право на жалба до Комисията за защита на личните данни (www.cpdp.bg).

# Деца

Приложението не е насочено към деца под 13 години. То може да се използва без вход и без никакви лични данни.

# Промени

При промяна на тази политика новата версия се публикува в приложението и в сайта на читалището."""

PRIVACY_EN = """Last updated: October 31, 2026

# Who processes the data

“Chitalishte Yavorets” is the official mobile app of the “Vasil Levski – 1922” Community Centre (chitalishte), Yavorets village, Gabrovo municipality, Bulgaria (“the centre”). Contact: chitalishte_yavorec@abv.bg.

# Principle

The app collects as little data as possible. It has no ads, no behavioural analytics, no tracking services and does not send crash reports to third parties.

# What data is processed

Public content: news, events, photos and pages are loaded from chyavorec.org, and the library catalogue from the public katalog.json file of the InvLib system hosted on GitHub (raw.githubusercontent.com and cdn.jsdelivr.net). Each such server sees the device’s IP address, as with any website visit. The catalogue contains no reader data. Book covers in the public catalogue are fetched by ISBN from covers.openlibrary.org (Internet Archive) — that server sees the IP address and the ISBN, nothing else; if the catalogue lists a scanned cover, it is loaded from the address given in the catalogue, and that server sees the IP address. Both are turned off in “Settings › Data › Covers from the internet”. Personal screens (reading history, borrowed books, “Want to read”, “For you”, the card, and a book’s page opened from them) never load covers from the internet — there covers are drawn on the phone.

Messages from the centre: the app checks the public messages file on chyavorec.org at most once an hour (less often — down to once every 6 hours — while there are no new messages) and stops checking if it has not been opened for 14 days. Members-only messages are shown if a library card has been entered on the device or after signing in — this check happens on the device and nothing is sent to the website. Read messages are remembered only on the device.

Personal messages from the library: after signing in, if the centre’s server supports it, the library can send you a personal message (e.g. about a reserved book). It comes over HTTPS from the centre’s server, is kept encrypted on the phone and is deleted when you sign out; marking it as read is sent to the same server. The notification for a new personal message is prepared on the phone; when the lock screen is set to hide sensitive content, it shows only “You have a personal message from the library” there, without the content.

Updates: the version installed outside Google Play checks for a new version in the update.json file on GitHub (github.com) and downloads it from there. Only a plain request is sent (GitHub sees the IP address); no personal data is sent. Automatic updates can be turned off in “Settings › Updates”. The Google Play version is updated by Google Play.

Data kept only on the device: settings (theme, language, notifications), saved news, event reminders, search history and cached public content for offline use. It never leaves the device and is not backed up to the cloud. The last book you opened from the public catalogue becomes a shortcut to it when you long-press the app icon; it also stays on the phone and is removed by “Sign out” and “Clear temporary data”. The app also keeps a local error log on the device only, without personal data; it leaves the device only if you share it yourself (“Settings › Information › Diagnostics”).

“Want to read”: the list of books you mark (and whether to tell you when one is on the shelf) is kept only on the device — availability is compared with the public catalogue on the phone, without sending which books are marked; signing out does not delete it, while “Clear list” (in the list itself) and uninstalling do. If “Notify me when it’s on the shelf” is on for a book, availability is also checked in the background: about once every 6 hours the app downloads the public katalog.json from GitHub (raw.githubusercontent.com or cdn.jsdelivr.net), without sending the list (only while “Back on the shelf” notifications are on and the app has been opened in the last 14 days).

Library card: if you enter your card number, it (and your name, if entered) is stored encrypted with an Android Keystore key on this device only.

Sign-in (only with consent given at the library and an issued PIN): your library card number and PIN are sent only over HTTPS to the centre’s server (chyavorec.org), and the PIN is never stored. The phone’s model name (e.g. “Pixel 8”) is also sent at sign-in so that the session can be recognised. Only an encrypted session token is kept. Your name, borrowed items, due dates and membership come from this server, which receives from the library software only the data of readers who have consented (no personal ID number (EGN), address or phone number), and are cached encrypted on the device. Consent is withdrawn at the library — your data is then deleted from the server at the next sync.

Password manager: saving your card number and PIN in the phone’s password manager is optional — it happens only if you agree, and they are kept by the system’s password manager, not by the app. If the password manager (e.g. Google Password Manager) syncs passwords, it may also sync the card number and PIN to your Google account (or the provider’s) — under the provider’s own terms.

Sign-in protection: after several failed sign-in attempts, sign-in is temporarily blocked (on the device and on the server) so that the PIN cannot be guessed.

Home-screen widget: if you add it, it shows only the number of books due and their due dates — no titles and no name.\n\nBarcode scanner: scanning is done by the Google Play services code scanner — it turns the camera on only while it is open and reads the code on the phone; the app has no camera access and receives only the decoded number, and frames are never stored or sent by the app.\n\nReading history: for readers who consented at the library, the list of returned books comes from the centre’s server and is kept encrypted on the phone. “Similar books” are picked on the phone only.\n\nRenewals: a renewal request goes to the centre’s server and is carried out by the library program at its next update.

Holds: if you place a hold on a book in the catalogue, the request (which book) goes to the centre’s server so that the library can set the book aside for you.

Documents: “Open with a PDF app” downloads the document from chyavorec.org into the app’s temporary data (kept for up to a week) and opens it in a PDF app of your choice on the phone.

Calendar subscription: if you add the centre’s calendar (https://chyavorec.org/events.ics) to a calendar app, that app or its service (e.g. Google Calendar) fetches the file from the website on its own schedule — as a plain request, without personal data; the centre’s app is not involved.

# Notifications

Notifications are prepared on the device (periodic checks for new posts, messages, due dates and the availability of “Want to read” books). No push notification servers are used. When the lock screen is set to hide sensitive content, personal notifications (about due dates, personal messages and “Want to read” books back on the shelf) show only a generic text there (e.g. “A library book is due soon”) — no titles or content. This is chosen in the phone’s system settings (notifications on the lock screen); when all content is shown, titles are visible too.

# Your rights

You can delete the data on the device at any time: “Sign out” deletes the session, reader data, loan and personal-message notifications, due-date reminders and the personal images in the cache — your profile photo and the covers of books you borrowed (without “Keep me signed in on this device” the same happens at the next start if the app was closed); “Remove” deletes the entered card; “Clear temporary data” deletes public content and downloaded documents; uninstalling removes everything. You can request deletion of your app account and your data on the server at https://chyavorec.org/app-delete, at the centre or by email. For the data the library keeps about you as a reader (under Bulgarian Ordinance No. 3 and the GDPR), you can request access, correction or deletion at the centre or by email. You may lodge a complaint with the Bulgarian Commission for Personal Data Protection (www.cpdp.bg).

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
"history_year_count": (("%1$d книга", "%1$d книги"), ("%1$d book", "%1$d books")),
"my_want_count": (("%1$d книга в списъка", "%1$d книги в списъка"), ("%1$d book on your list", "%1$d books on your list")),
"book_available_copies": (("%1$d наличен", "%1$d налични"), ("%1$d available", "%1$d available")),
"book_copies": (("%1$d екземпляр", "%1$d екземпляра"), ("%1$d copy", "%1$d copies")),
"book_more_on_shelf": (("Друг екземпляр е на рафта", "Други %1$d екземпляра са на рафта"), ("Another copy is on the shelf", "%1$d other copies are on the shelf")),
"catalog_count": (("%1$d резултат", "%1$d резултата"), ("%1$d result", "%1$d results")),
"catalog_show_results": (("Покажи %1$d резултат", "Покажи %1$d резултата"), ("Show %1$d result", "Show %1$d results")),
"due_days_left": (("остава %1$d ден", "остават %1$d дни"), ("%1$d day left", "%1$d days left")),
"due_overdue_days": (("просрочена с %1$d ден", "просрочена с %1$d дни"), ("%1$d day overdue", "%1$d days overdue")),
"messages_unread": (("%1$d непрочетено съобщение", "%1$d непрочетени съобщения"), ("%1$d unread message", "%1$d unread messages")),
"widget_books_to_return": (("%1$d книга за връщане", "%1$d книги за връщане"), ("%1$d book to return", "%1$d books to return")),
"widget_books_count": (("%1$d книга", "%1$d книги"), ("%1$d book", "%1$d books")),
"widget_due_in_days": (("след %1$d ден", "след %1$d дни"), ("in %1$d day", "in %1$d days")),
"widget_due_overdue": (("просрочена", "просрочени"), ("overdue", "overdue")),
"photos_count": (("%1$d снимка", "%1$d снимки"), ("%1$d photo", "%1$d photos")),
"wait_seconds": (("%1$d секунда", "%1$d секунди"), ("%1$d second", "%1$d seconds")),
"wait_minutes": (("%1$d минута", "%1$d минути"), ("%1$d minute", "%1$d minutes")),
"wait_hours": (("%1$d час", "%1$d часа"), ("%1$d hour", "%1$d hours")),
"login_attempts_left": (("Остава %1$d опит преди временно заключване", "Остават %1$d опита преди временно заключване"), ("%1$d attempt left before a temporary lock", "%1$d attempts left before a temporary lock")),
"loan_renewed_times": (("Удължавана %1$d път", "Удължавана %1$d пъти"), ("Renewed %1$d time", "Renewed %1$d times")),
"home_loans_overdue": (("%1$d книга е просрочена", "%1$d книги са просрочени"), ("%1$d book is overdue", "%1$d books are overdue")),
"home_loans_total": (("%1$d заета книга", "%1$d заети книги"), ("%1$d book borrowed", "%1$d books borrowed")),
"reading_months_cd": (("%1$d книга за последните 12 месеца, най-много през %2$s", "%1$d книги за последните 12 месеца, най-много през %2$s"), ("%1$d book in the last 12 months, most in %2$s", "%1$d books in the last 12 months, most in %2$s")),
"reading_streak_months": (("%1$d месец", "%1$d месеца"), ("%1$d month", "%1$d months")),
# Под брояча на „Начало“ (числото е отделно, над текста).
"stat_books": (("документ в каталога", "документа в каталога"), ("catalogue item", "catalogue items")),
"stat_events": (("събитие през следващите 30 дни", "събития през следващите 30 дни"), ("event in the next 30 days", "events in the next 30 days")),
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
