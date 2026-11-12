# Wave 16 emulator check, round d (858c8b2): unlock with the framework BiometricPrompt, size, smoke

Emulator run 41363967278 (debug/wave16-check @ 1fd4543). Round c: [../v130c/index.md](../v130c/index.md). Round b: [../v130b/index.md](../v130b/index.md). Round a: [../v130/index.md](../v130/index.md).

```
== new 1.2.30 858c8b2: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4130042 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1488543  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.30" (Raw: "1.2.30")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=true 
uses-permission: 'android.permission.INTERNET' 'android.permission.ACCESS_NETWORK_STATE' 'android.permission.POST_NOTIFICATIONS' 'android.permission.USE_BIOMETRIC' 'android.permission.REQUEST_INSTALL_PACKAGES' 'android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' 'android.permission.FOREGROUND_SERVICE' 'android.permission.FOREGROUND_SERVICE_DATA_SYNC' 'android.permission.WAKE_LOCK' 'android.permission.RECEIVE_BOOT_COMPLETED' 'org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
foreground services: 
part                    compressed  uncompressed  files
classes*.dex               3373906       6668664      2
res                         468365        832408     57
resources.arsc              222432        222432      1
assets/                      19730         19730      2
lib/arm64-v8a                 8642         20472      2
lib/armeabi-v7a               6601         13116      2
AndroidManifest.xml           5463         23760      1
META-INF/                      846          1288      6
```

## smoke33

```
part smoke33; APK: 1.2.30 858c8b2 (Отключване на личните екрани: системният BiometricPrompt вме); versionCode 1488543; x86_64 libs: 2 size 4137778 bytes
base APK:  size  bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2500 ms
## S1. Cold starts (10)
START cold               n=10 min=952ms median=1118ms max=1328ms  all=[1124, 1313, 1111, 1047, 1328, 1082, 952, 1155, 969, 1237]  states=['COLD']
## S2. Monkey 2000 events (seed 1357, CAMERA revoked)
MONKEY exit=0 duration=85 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '7894'
## COMPLETE
## logcat findings
app pids seen: ['5705', '7104', '7224', '7288', '7342', '7412', '7556', '7618', '7670', '7726', '7814', '7894']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=37 total=199
== Davey: 13
   davey events=13 max=1015ms median=789ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## unlock28

```
part unlock28; APK: 1.2.30 858c8b2 (Отключване на личните екрани: системният BiometricPrompt вме); versionCode 1488543; x86_64 libs: 2 size 4137778 bytes
base APK:  size  bytes
host: 4 cpu, 15982 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: 
camera permission: 
screen 1080x2400
SYSTEM locale -> bg-BG: 
SYSTEM locale now: persist.sys.locale=bg-BG; config: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 1514 ms
## P0. Package (GitHub build): requested permissions
requested permissions: android.permission.ACCESS_NETWORK_STATE android.permission.FOREGROUND_SERVICE android.permission.FOREGROUND_SERVICE_DATA_SYNC android.permission.INTERNET android.permission.POST_NOTIFICATIONS android.permission.RECEIVE_BOOT_COMPLETED android.permission.REQUEST_INSTALL_PACKAGES android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION android.permission.USE_BIOMETRIC android.permission.WAKE_LOCK 
PERM android.permission.USE_BIOMETRIC: 1
PERM android.permission.USE_FINGERPRINT: 0
PERM android.permission.CAMERA: 0
PERM android.permission.FOREGROUND_SERVICE: 1
PERM android.permission.FOREGROUND_SERVICE_DATA_SYNC: 1
APK uses-permission (aapt2): name='android.permission.INTERNET' name='android.permission.ACCESS_NETWORK_STATE' name='android.permission.POST_NOTIFICATIONS' name='android.permission.USE_BIOMETRIC' name='android.permission.REQUEST_INSTALL_PACKAGES' name='android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' name='android.permission.FOREGROUND_SERVICE' name='android.permission.FOREGROUND_SERVICE_DATA_SYNC' name='android.permission.WAKE_LOCK' name='android.permission.RECEIVE_BOOT_COMPLETED' name='org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
APK SystemForegroundService: foregroundServiceType(0x01010599)=0x00000001
## P1. No screen lock: Settings › Поверителност — the switch is disabled with an explanation
device secure:  lock: true
SWITCH (no lock): SWITCH class=android.view.View checkable=true checked=false enabled=false clickable=true bounds=[0,1180][1080,1477] texts=['Искай отключване за личните екрани', 'Не може да се включи: телефонът няма заключване на екрана. Задай ПИН, фигура или парола в настройките на телефона.']
explanation 'Не може да се включи…': SHOWN; group title 'Поверителност' yes
tap on the disabled switch: SWITCH class=android.view.View checkable=true checked=false enabled=false clickable=true bounds=[0,1180][1080,1477] texts=['Искай отключване за личните екрани', 'Не може да се включи: телефонът няма заключване на екрана. Задай ПИН, фигура или парола в настройките на телефона.']; prompt absent; fg org.chyavorec.app/org.chyavorec.app.MainActivity; PROMPT packages=['org.chyavorec.app'] password-fields=0 texts=['Корици по ISBN от covers.openlibrary.org и сканираните корици от кат
## P2. A self-entered card (before the switch is on)
CARD added 'TEST-12345': 
card screen: card NO
## P3. Device PIN 1234; the switch → the system prompt → PIN
set-pin: Pin set to '1234'
SWITCH (PIN set): SWITCH class=android.view.View checkable=true checked=false enabled=true clickable=true bounds=[0,1132][1080,1485] texts=['Искай отключване за личните екрани', 'Картата, заеманията, историята и личните съобщения се отварят след пръстов отпечатък, лице или ПИН-а на телефона.']
SWITCH tapped → prompt SHOWN; fg com.android.settings/com.android.settings.password.ConfirmLockPassword; PROMPT packages=['com.android.settings'] password-fields=1 texts=['Отключи личните екрани', 'Искай отключване за личните екрани']
PIN screen: PROMPT packages=['com.android.settings'] password-fields=1 texts=['Отключи личните екрани', 'Искай отключване за личните екрани']
SWITCH after the PIN: SWITCH class=android.view.View checkable=true checked=true enabled=true clickable=true bounds=[0,1132][1080,1485] texts=['Искай отключване за личните екрани', 'Картата, заеманията, историята и личните съобщения се отварят след пръстов отпечатък, лице или ПИН-а на телефона.']; fg org.chyavorec.app/org.chyavorec.app.MainActivity
## P4. New process: Моето › Читателска карта → prompt; Back (cancel) → Моето; PIN → the card
GATE 20-card-prompt: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg com.android.settings/com.android.settings.password.ConfirmLockPassword; text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
cancel: Back #1 → prompt still; IME mInputShown=false
cancel: Back #2 → prompt gone; IME mInputShown=false
GATE 21-after-cancel: prompt absent; locked view no; card no; TAB selected: ['Моето']; back arrow no; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" 
PIN screen: PROMPT packages=['com.android.settings'] password-fields=1 texts=['Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 22-after-pin: prompt absent; locked view no; card no; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Дигитална карта" text="Въведи номера от своята читателска карта. Приложението ще покаже баркод в същия формат (Code 39), в който библиотеката печата картите." text="Номер на картата" text="Главн
## P5. Background 20 s (< 60 s): no prompt; background 70 s (> 60 s): prompt
GATE 30-after-20s-background: prompt absent; locked view no; card no; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Дигитална карта" text="Въведи номера от своята читателска карта. Приложението ще покаже баркод в същия формат (Code 39), в който библиотеката печата картите." text="Номер на картата" text="Главн
GATE 31-after-70s-background: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg com.android.settings/com.android.settings.password.ConfirmLockPassword; text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
PIN screen: PROMPT packages=['com.android.settings'] password-fields=1 texts=['Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 32-after-pin: prompt absent; locked view no; card no; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Дигитална карта" text="Въведи номера от своята читателска карта. Приложението ще покаже баркод в същия формат (Code 39), в който библиотеката печата картите." text="Номер на картата" text="Главн
## P6. Screen off and on (keyguard with the PIN): prompt again
keyguard: prompt absent; fg StatusBar; PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Android Setup', 'Подготвя се за настройване…', 'Android', 'Сигналът за Wi-Fi е пълен.', 'Телефонът е с две чертички.', 'Батерията се зарежда – 100 процента.', '100%
GATE 40-after-screen-off: prompt absent; locked view no; card no; (unreadable dump: [Errno 2] No such file or directory: 'out/unlock28/ui.xml'); back arrow no; app in front NO; fg com.android.settings/com.android.settings.password.ConfirmLockPassword; 
biometric/keyguard log after screen off: 
## P7. Quick Settings tile → the card: the gate
sysui_qs_tiles before: null
add-tile org.chyavorec.app/.tile.CardTileService: ''
sysui_qs_tiles after: custom(org.chyavorec.app/.tile.CardTileService),wifi,bt,dnd,flashlight,rotation,battery,cell,airplane,cast
QS gate: tile @ 196 517 (page 1): MATCH class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[49,378][344,656]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[49,378][344,656]" | ACTIONABLE-CHILD none MATCH class="android.widget.TextView" text="Читателска карта" content-desc="" checkable="false" checked="false" clickable="false" bounds="[64,536][328,579]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[49,378][344,656]" | ACTIONABLE-CHILD none  texts: content-desc="Читателска карта" text="Читателска карта" 
QS gate: ours in sysui_qs_tiles: 1
GATE 50-tile-tap: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg com.android.settings/com.android.settings.password.ConfirmLockPassword; text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
cancel: Back #1 → prompt still; IME mInputShown=false
cancel: Back #2 → prompt gone; IME mInputShown=false
GATE 51-tile-after-cancel: prompt absent; locked view no; card no; TAB selected: ['Моето']; back arrow no; app in front yes; fg org.chyavorec.app/org.chyavorec.app.OpenLink; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" 
QS gate2: tile @ 196 517 (page 1): MATCH class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[49,378][344,656]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[49,378][344,656]" | ACTIONABLE-CHILD none MATCH class="android.widget.TextView" text="Читателска карта" content-desc="" checkable="false" checked="false" clickable="false" bounds="[64,536][328,579]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[49,378][344,656]" | ACTIONABLE-CHILD none  texts: content-desc="Читателска карта" text="Читателска карта" 
QS gate2: ours in sysui_qs_tiles: 1
PIN screen: PROMPT packages=['com.android.settings'] password-fields=1 texts=['Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 52-tile-after-pin: prompt absent; locked view no; card no; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.OpenLink; text="Дигитална карта" text="Въведи номера от своята читателска карта. Приложението ще покаже баркод в същия формат (Code 39), в който библиотеката печата картите." text="Номер на картата" text="Главн
## P8. Turning the switch off also asks for the PIN
switch OFF tapped → prompt SHOWN; fg com.android.settings/com.android.settings.password.ConfirmLockPassword; PROMPT packages=['com.android.settings'] password-fields=1 texts=['Отключи личните екрани', 'Искай отключване з
PIN screen: PROMPT packages=['com.android.settings'] password-fields=1 texts=['Отключи личните екрани', 'Искай отключване за личните екрани']
SWITCH now: SWITCH class=android.view.View checkable=true checked=false enabled=true clickable=true bounds=[0,1935][1080,2274] texts=['Искай отключване за личните екрани', 'Картата, заеманията, историята и личните съобщения се отварят след пръстов отпечатък, лице или ПИН-а на телефона.']
clear PIN: Lock credential cleared
## COMPLETE
## logcat findings
app pids seen: ['7621', '8891', '9895']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=44 total=44
== Davey: 2
   davey events=2 max=1123ms median=959ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## unlock33

```
part unlock33; APK: 1.2.30 858c8b2 (Отключване на личните екрани: системният BiometricPrompt вме); versionCode 1488543; x86_64 libs: 2 size 4137778 bytes
base APK:  size  bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3673 ms
## P0. Package (GitHub build): requested permissions
requested permissions: android.permission.ACCESS_NETWORK_STATE android.permission.FOREGROUND_SERVICE android.permission.FOREGROUND_SERVICE_DATA_SYNC android.permission.INTERNET android.permission.POST_NOTIFICATIONS android.permission.RECEIVE_BOOT_COMPLETED android.permission.REQUEST_INSTALL_PACKAGES android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION android.permission.USE_BIOMETRIC android.permission.WAKE_LOCK 
PERM android.permission.USE_BIOMETRIC: 1
PERM android.permission.USE_FINGERPRINT: 0
PERM android.permission.CAMERA: 0
PERM android.permission.FOREGROUND_SERVICE: 1
PERM android.permission.FOREGROUND_SERVICE_DATA_SYNC: 1
APK uses-permission (aapt2): name='android.permission.INTERNET' name='android.permission.ACCESS_NETWORK_STATE' name='android.permission.POST_NOTIFICATIONS' name='android.permission.USE_BIOMETRIC' name='android.permission.REQUEST_INSTALL_PACKAGES' name='android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' name='android.permission.FOREGROUND_SERVICE' name='android.permission.FOREGROUND_SERVICE_DATA_SYNC' name='android.permission.WAKE_LOCK' name='android.permission.RECEIVE_BOOT_COMPLETED' name='org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
APK SystemForegroundService: foregroundServiceType(0x01010599)=0x00000001
## P1. No screen lock: Settings › Поверителност — the switch is disabled with an explanation
device secure:  lock: true
SWITCH (no lock): SWITCH class=android.view.View checkable=true checked=false enabled=false clickable=true bounds=[0,1570][1080,1867] texts=['Искай отключване за личните екрани', 'Не може да се включи: телефонът няма заключване на екрана. Задай ПИН, фигура или парола в настройките на телефона.']
explanation 'Не може да се включи…': SHOWN; group title 'Поверителност' yes
tap on the disabled switch: SWITCH class=android.view.View checkable=true checked=false enabled=false clickable=true bounds=[0,1570][1080,1867] texts=['Искай отключване за личните екрани', 'Не може да се включи: телефонът няма заключване на екрана. Задай ПИН, фигура или парола в настройките на телефона.']; prompt absent; fg org.chyavorec.app/org.chyavorec.app.MainActivity; PROMPT packages=['org.chyavorec.app'] password-fields=0 texts=['Новата версия е подписана с различен ключ и не може да замени инстал
## P2. A self-entered card (before the switch is on)
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
card screen: card shown
## P3. Device PIN 1234; the switch → the system prompt → PIN
set-pin: Pin set to '1234'
SWITCH (PIN set): SWITCH class=android.view.View checkable=true checked=false enabled=true clickable=true bounds=[0,1762][1080,2115] texts=['Искай отключване за личните екрани', 'Картата, заеманията, историята и личните съобщения се отварят след пръстов отпечатък, лице или ПИН-а на телефона.']
SWITCH tapped → prompt SHOWN; fg BiometricPrompt; PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
SWITCH after the PIN: SWITCH class=android.view.View checkable=true checked=true enabled=true clickable=true bounds=[0,1762][1080,2115] texts=['Искай отключване за личните екрани', 'Картата, заеманията, историята и личните съобщения се отварят след пръстов отпечатък, лице или ПИН-а на телефона.']; fg org.chyavorec.app/org.chyavorec.app.MainActivity
## P4. New process: Моето › Читателска карта → prompt; Back (cancel) → Моето; PIN → the card
GATE 20-card-prompt: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg BiometricPrompt; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
cancel: Back #1 → prompt still; IME mInputShown=false
cancel: Back #2 → prompt gone; IME mInputShown=false
GATE 21-after-cancel: prompt absent; locked view no; card no; TAB selected: ['Моето']; back arrow no; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Въведена от теб карта" 
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 22-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P5. Background 20 s (< 60 s): no prompt; background 70 s (> 60 s): prompt
GATE 30-after-20s-background: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
GATE 31-after-70s-background: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg BiometricPrompt; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 32-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P6. Screen off and on (keyguard with the PIN): prompt again
keyguard: prompt absent; fg NotificationShade; PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Configure physical keyboard', 'Alerted', 'Tap to select language and layout', 'Expand', '12:02', 'Mon, Nov 2', 'T-Mobile', 'Wifi signal full.', 'Phone signa
GATE 40-after-screen-off: prompt absent; locked view no; card no; TAB selected: ['Моето']; back arrow no; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Въведена от теб карта" 
biometric/keyguard log after screen off:    485   485 D BiometricService: Sensor ID(0), oemStrength: 15, updatedStrength: 15, modality 2, state: 0, cookie: 0, sensorId 0, hasEnrollments: false cannot participate in Keystore~   763   763 E KeyguardViewMediator: mHideAnimationFinishedRunnable#run~   763   763 D KeyguardViewMediator: onKeyguardExitRemoteAnimationFinished#hideKeyguardViewAfterRemoteAnimation~   763   763 D KeyguardViewMediator: onKeyguardExitRemoteAnimationFinished#hideKeyguardViewAfterRemoteAnimation~   485   485 D BiometricService: Sensor ID(0), oemStrength: 15, updatedStrength: 15, modality 2, state: 0, cookie: 0, sensorId 0, hasEnrollments: false cannot participate in Keystore~
## P7. Quick Settings tile → the card: the gate
sysui_qs_tiles before: internet,bt,flashlight,dnd,alarm,airplane,controls,rotation,qr_code_scanner,battery,cast,screenrecord,custom(com.google.android.gms/.nearby.sharing.SharingTileService),mictoggle,cameratoggle,dbg:mem,ott
add-tile org.chyavorec.app/.tile.CardTileService: ''
sysui_qs_tiles after: custom(org.chyavorec.app/.tile.CardTileService),internet,bt,flashlight,dnd,alarm,airplane,controls,rotation,qr_code_scanner,battery,cast,screenrecord,custom(com.google.android.gms/.nearby.sharing.SharingTileService),mictoggle,cameratoggle,dbg:mem,ott
QS gate: tile @ NOT FOUND (page 4):  texts: 
QS gate: ours in sysui_qs_tiles: 1
click-tile: 
GATE 50-tile-tap: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg BiometricPrompt; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
cancel: Back #1 → prompt still; IME mInputShown=false
cancel: Back #2 → prompt gone; IME mInputShown=false
GATE 51-tile-after-cancel: prompt absent; locked view no; card no; TAB selected: ['Моето']; back arrow no; app in front yes; fg org.chyavorec.app/org.chyavorec.app.OpenLink; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Въведена от теб карта" 
QS gate2: tile @ 285 630 (page 1): MATCH class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none MATCH class="android.widget.TextView" text="Читателска карта" content-desc="" checkable="false" checked="false" clickable="false" bounds="[163,579][418,630]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none  texts: content-desc="Читателска карта" text="Читателска карта" text="Покажи на гишето" 
QS gate2: ours in sysui_qs_tiles: 1
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 52-tile-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.OpenLink; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P8. Turning the switch off also asks for the PIN
switch OFF tapped → prompt SHOWN; fg BiometricPrompt; PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
SWITCH now: SWITCH class=android.view.View checkable=true checked=false enabled=true clickable=true bounds=[0,1310][1080,1663] texts=['Искай отключване за личните екрани', 'Картата, заеманията, историята и личните съобщения се отварят след пръстов отпечатък, лице или ПИН-а на телефона.']
clear PIN: Lock credential cleared
## COMPLETE
## logcat findings
app pids seen: ['4902', '8696', '9920']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 10
   skipped-frames events=10 max=83 total=445
== Davey: 17
   davey events=17 max=1811ms median=867ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## unlock34

```
part unlock34; APK: 1.2.30 858c8b2 (Отключване на личните екрани: системният BiometricPrompt вме); versionCode 1488543; x86_64 libs: 2 size 4137778 bytes
base APK:  size  bytes
host: 4 cpu, 15983 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2835 ms
## P0. Package (GitHub build): requested permissions
requested permissions: android.permission.ACCESS_NETWORK_STATE android.permission.FOREGROUND_SERVICE android.permission.FOREGROUND_SERVICE_DATA_SYNC android.permission.INTERNET android.permission.POST_NOTIFICATIONS android.permission.RECEIVE_BOOT_COMPLETED android.permission.REQUEST_INSTALL_PACKAGES android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION android.permission.USE_BIOMETRIC android.permission.WAKE_LOCK 
PERM android.permission.USE_BIOMETRIC: 1
PERM android.permission.USE_FINGERPRINT: 0
PERM android.permission.CAMERA: 0
PERM android.permission.FOREGROUND_SERVICE: 1
PERM android.permission.FOREGROUND_SERVICE_DATA_SYNC: 1
APK uses-permission (aapt2): name='android.permission.INTERNET' name='android.permission.ACCESS_NETWORK_STATE' name='android.permission.POST_NOTIFICATIONS' name='android.permission.USE_BIOMETRIC' name='android.permission.REQUEST_INSTALL_PACKAGES' name='android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' name='android.permission.FOREGROUND_SERVICE' name='android.permission.FOREGROUND_SERVICE_DATA_SYNC' name='android.permission.WAKE_LOCK' name='android.permission.RECEIVE_BOOT_COMPLETED' name='org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
APK SystemForegroundService: foregroundServiceType(0x01010599)=0x00000001
## P1. No screen lock: Settings › Поверителност — the switch is disabled with an explanation
device secure:  lock: true
SWITCH (no lock): SWITCH class=android.view.View checkable=true checked=false enabled=false clickable=true bounds=[0,1874][1080,2171] texts=['Искай отключване за личните екрани', 'Не може да се включи: телефонът няма заключване на екрана. Задай ПИН, фигура или парола в настройките на телефона.']
explanation 'Не може да се включи…': SHOWN; group title 'Поверителност' yes
tap on the disabled switch: SWITCH class=android.view.View checkable=true checked=false enabled=false clickable=true bounds=[0,1874][1080,2171] texts=['Искай отключване за личните екрани', 'Не може да се включи: телефонът няма заключване на екрана. Задай ПИН, фигура или парола в настройките на телефона.']; prompt absent; fg org.chyavorec.app/org.chyavorec.app.MainActivity; PROMPT packages=['org.chyavorec.app'] password-fields=0 texts=['Провери за нова версия', 'Новата версия е подписана с различен ключ 
## P2. A self-entered card (before the switch is on)
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
card screen: card shown
## P3. Device PIN 1234; the switch → the system prompt → PIN
set-pin: Pin set to '1234'
SWITCH (PIN set): SWITCH class=android.view.View checkable=true checked=false enabled=true clickable=true bounds=[0,2116][1080,2337] texts=['Искай отключване за личните екрани', 'Картата, заеманията, историята и личните съобщения се отварят след пръстов отпечатък, лице или ПИН-а на телефона.']
SWITCH tapped → prompt SHOWN; fg BiometricPrompt; PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
SWITCH after the PIN: SWITCH class=android.view.View checkable=true checked=true enabled=true clickable=true bounds=[0,2116][1080,2337] texts=['Искай отключване за личните екрани', 'Картата, заеманията, историята и личните съобщения се отварят след пръстов отпечатък, лице или ПИН-а на телефона.']; fg org.chyavorec.app/org.chyavorec.app.MainActivity
## P4. New process: Моето › Читателска карта → prompt; Back (cancel) → Моето; PIN → the card
GATE 20-card-prompt: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg BiometricPrompt; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
cancel: Back #1 → prompt still; IME mInputShown=false
cancel: Back #2 → prompt gone; IME mInputShown=false
GATE 21-after-cancel: prompt absent; locked view no; card no; TAB selected: ['Моето']; back arrow no; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Въведена от теб карта" 
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 22-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P5. Background 20 s (< 60 s): no prompt; background 70 s (> 60 s): prompt
GATE 30-after-20s-background: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
GATE 31-after-70s-background: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg BiometricPrompt; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 32-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P6. Screen off and on (keyguard with the PIN): prompt again
keyguard: prompt absent; fg NotificationShade; PROMPT packages=['com.android.systemui'] password-fields=1 texts=['12\n02', 'Mon, Nov 2', 'T-Mobile', 'Wifi signal full.', 'Phone signal full.', 'Battery charging, 100 percent.', '100%', 'Charged', 'Enter your PIN', 'PIN ar
GATE 40-after-screen-off: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg BiometricPrompt; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
biometric/keyguard log after screen off:    517   517 D BiometricService: Sensor ID(4), oemStrength: 15, updatedStrength: 15, modality 8, state: 0, cookie: 0, sensorId 4, hasEnrollments: false cannot participate in Keystore~   517   517 D BiometricService: Sensor ID(0), oemStrength: 15, updatedStrength: 15, modality 2, state: 0, cookie: 0, sensorId 0, hasEnrollments: false cannot participate in Keystore~   517   746 D BiometricService: resetLockout(userId=0, hat=present)~   829   829 E KeyguardViewMediator: mHideAnimationFinishedRunnable#run~   517   517 D BiometricService/PreAuthInfo: Package: org.chyavorec.app Sensor ID: 4 Modality: 8 Status: 7~   517   517 D BiometricService/PreAuthInfo: Package: org.chyavorec.app Sensor ID: 0 Modality: 2 Status: 7~   517   517 D BiometricService/PreAuthInfo: getCanAuthenticateInternal Modality: 1 AuthenticatorStatus: 1~   517   517 D BiometricService: handleAuthenticate: modality(1), status(0), preAuthInfo: BiometricRequested: true, StrengthRequested: 255, CredentialRequested: true, Eligible:{}, In~   517   517 D BiometricService: Creating authSession with authRequest: BiometricRequested: true, StrengthRequested: 255, CredentialRequested: true, Eligible:{}, Ineligible:{ID(4), o~   517   517 D BiometricService/AuthSession: Creating AuthSession with: BiometricRequested: true, StrengthRequested: 255, CredentialRequested: true, Eligible:{}, Ineligible:{ID(4), o~   829   829 D AuthController: showAuthenticationDialog, authenticators: 32768, sensorIds: , credentialAllowed: true, requireConfirmation: false, operationId: 0, requestId: 5~   829   829 D AuthController: userId: 0 savedState: null mCurrentDialog: null newDialog: com.android.systemui.biometrics.AuthContainerView{d14c47 VFE...... .F....I. 0,0-0,0}~   517  2003 D CoreBackPreview: Window{b223e31 u0 BiometricPrompt}: Setting back callback OnBackInvokedCallbackInfo{mCallback=android.window.IOnBackInvokedCallback$Stub$Proxy@9d31c84~   829   829 W AuthController: Task stack changed, current client: org.chyavorec.app~
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 41-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P7. Quick Settings tile → the card: the gate
sysui_qs_tiles before: internet,bt,flashlight,dnd,alarm,airplane,controls,rotation,qr_code_scanner,battery,cast,screenrecord,custom(com.google.android.gms/.nearby.sharing.SharingTileService),mictoggle,cameratoggle,custom(com.google.android.permissioncontroller/com.android.permissioncontroller.permission.service.v33.SafetyCenterQsTileService),dbg:mem,ott
add-tile org.chyavorec.app/.tile.CardTileService: ''
sysui_qs_tiles after: custom(org.chyavorec.app/.tile.CardTileService),internet,bt,flashlight,dnd,alarm,airplane,controls,rotation,qr_code_scanner,battery,cast,screenrecord,custom(com.google.android.gms/.nearby.sharing.SharingTileService),mictoggle,cameratoggle,custom(com.google.android.permissioncontroller/com.android.permissioncontroller.permission.service.v33.SafetyCenterQsTileService),dbg:mem,ott
QS gate: tile @ 285 630 (page 1): MATCH class="android.widget.LinearLayout" text="Читателска карта, Покажи на гишето" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="Читателска карта, Покажи на гишето" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none MATCH class="android.widget.TextView" text="Читателска карта" content-desc="" checkable="false" checked="false" clickable="false" bounds="[163,579][418,630]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="Читателска карта, Покажи на гишето" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none  texts: text="Читателска карта, Покажи на гишето" content-desc="Читателска карта" text="Читателска карта" text="Покажи на гишето" 
QS gate: ours in sysui_qs_tiles: 1
GATE 50-tile-tap: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg BiometricPrompt; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
cancel: Back #1 → prompt still; IME mInputShown=false
cancel: Back #2 → prompt gone; IME mInputShown=false
GATE 51-tile-after-cancel: prompt absent; locked view no; card no; TAB selected: ['Моето']; back arrow no; app in front yes; fg org.chyavorec.app/org.chyavorec.app.OpenLink; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Въведена от теб карта" 
QS gate2: tile @ 285 630 (page 1): MATCH class="android.widget.LinearLayout" text="Читателска карта, Покажи на гишето" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="Читателска карта, Покажи на гишето" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none MATCH class="android.widget.TextView" text="Читателска карта" content-desc="" checkable="false" checked="false" clickable="false" bounds="[163,579][418,630]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="Читателска карта, Покажи на гишето" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none  texts: text="Читателска карта, Покажи на гишето" content-desc="Читателска карта" text="Читателска карта" text="Покажи на гишето" 
QS gate2: ours in sysui_qs_tiles: 1
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 52-tile-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.OpenLink; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P8. Turning the switch off also asks for the PIN
switch OFF tapped → prompt SHOWN; fg BiometricPrompt; PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
SWITCH now: SWITCH class=android.view.View checkable=true checked=false enabled=true clickable=true bounds=[0,1648][1080,2001] texts=['Искай отключване за личните екрани', 'Картата, заеманията, историята и личните съобщения се отварят след пръстов отпечатък, лице или ПИН-а на телефона.']
clear PIN: Lock credential cleared
## COMPLETE
## logcat findings
app pids seen: ['5289', '7164', '8237']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=73 total=104
== Davey: 8
   davey events=8 max=1360ms median=802ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Files

- [runs.txt](runs.txt)
- [size/apk-ref.txt](size/apk-ref.txt)
- [size/size-new.txt](size/size-new.txt)
- [smoke33/host-after-run.txt](smoke33/host-after-run.txt)
- [smoke33/job-status.txt](smoke33/job-status.txt)
- [smoke33/logcat-all.txt](smoke33/logcat-all.txt)
- [smoke33/logcat-dump.txt](smoke33/logcat-dump.txt)
- [smoke33/logcat-findings.txt](smoke33/logcat-findings.txt)
- [smoke33/logcat-live.txt](smoke33/logcat-live.txt)
- [smoke33/monkey.txt](smoke33/monkey.txt)
- [smoke33/start-cold.txt](smoke33/start-cold.txt)
- [smoke33/summary.txt](smoke33/summary.txt)
- [unlock28/dumpsys-package.txt](unlock28/dumpsys-package.txt)
- [unlock28/host-after-run.txt](unlock28/host-after-run.txt)
- [unlock28/job-status.txt](unlock28/job-status.txt)
- [unlock28/logcat-all.txt](unlock28/logcat-all.txt)
- [unlock28/logcat-dump.txt](unlock28/logcat-dump.txt)
- [unlock28/logcat-findings.txt](unlock28/logcat-findings.txt)
- [unlock28/logcat-live.txt](unlock28/logcat-live.txt)
- [unlock28/prompt-enable.xml](unlock28/prompt-enable.xml)
- [unlock28/qs-gate-page1.xml](unlock28/qs-gate-page1.xml)
- [unlock28/qs-gate2-page1.xml](unlock28/qs-gate2-page1.xml)
- [unlock28/shots/unlock/01-no-screen-lock.png](unlock28/shots/unlock/01-no-screen-lock.png)
- [unlock28/shots/unlock/10-with-screen-lock.png](unlock28/shots/unlock/10-with-screen-lock.png)
- [unlock28/shots/unlock/12-after-pin.png](unlock28/shots/unlock/12-after-pin.png)
- [unlock28/shots/unlock/21-after-cancel.png](unlock28/shots/unlock/21-after-cancel.png)
- [unlock28/shots/unlock/39-keyguard.png](unlock28/shots/unlock/39-keyguard.png)
- [unlock28/shots/unlock/51-tile-after-cancel.png](unlock28/shots/unlock/51-tile-after-cancel.png)
- [unlock28/summary.txt](unlock28/summary.txt)
- [unlock33/dumpsys-package.txt](unlock33/dumpsys-package.txt)
- [unlock33/host-after-run.txt](unlock33/host-after-run.txt)
- [unlock33/job-status.txt](unlock33/job-status.txt)
- [unlock33/logcat-all.txt](unlock33/logcat-all.txt)
- [unlock33/logcat-dump.txt](unlock33/logcat-dump.txt)
- [unlock33/logcat-findings.txt](unlock33/logcat-findings.txt)
- [unlock33/logcat-live.txt](unlock33/logcat-live.txt)
- [unlock33/prompt-enable.xml](unlock33/prompt-enable.xml)
- [unlock33/qs-gate-page1.xml](unlock33/qs-gate-page1.xml)
- [unlock33/qs-gate-page2.xml](unlock33/qs-gate-page2.xml)
- [unlock33/qs-gate-page3.xml](unlock33/qs-gate-page3.xml)
- [unlock33/qs-gate-page4.xml](unlock33/qs-gate-page4.xml)
- [unlock33/qs-gate2-page1.xml](unlock33/qs-gate2-page1.xml)
- [unlock33/shots/unlock/01-no-screen-lock.png](unlock33/shots/unlock/01-no-screen-lock.png)
- [unlock33/shots/unlock/10-with-screen-lock.png](unlock33/shots/unlock/10-with-screen-lock.png)
- [unlock33/shots/unlock/12-after-pin.png](unlock33/shots/unlock/12-after-pin.png)
- [unlock33/shots/unlock/39-keyguard.png](unlock33/shots/unlock/39-keyguard.png)
- [unlock33/summary.txt](unlock33/summary.txt)
- [unlock34/dumpsys-package.txt](unlock34/dumpsys-package.txt)
- [unlock34/host-after-run.txt](unlock34/host-after-run.txt)
- [unlock34/job-status.txt](unlock34/job-status.txt)
- [unlock34/logcat-all.txt](unlock34/logcat-all.txt)
- [unlock34/logcat-dump.txt](unlock34/logcat-dump.txt)
- [unlock34/logcat-findings.txt](unlock34/logcat-findings.txt)
- [unlock34/logcat-live.txt](unlock34/logcat-live.txt)
- [unlock34/prompt-enable.xml](unlock34/prompt-enable.xml)
- [unlock34/qs-gate-page1.xml](unlock34/qs-gate-page1.xml)
- [unlock34/qs-gate2-page1.xml](unlock34/qs-gate2-page1.xml)
- [unlock34/shots/unlock/01-no-screen-lock.png](unlock34/shots/unlock/01-no-screen-lock.png)
- [unlock34/shots/unlock/10-with-screen-lock.png](unlock34/shots/unlock/10-with-screen-lock.png)
- [unlock34/shots/unlock/12-after-pin.png](unlock34/shots/unlock/12-after-pin.png)
- [unlock34/shots/unlock/39-keyguard.png](unlock34/shots/unlock/39-keyguard.png)
- [unlock34/summary.txt](unlock34/summary.txt)
