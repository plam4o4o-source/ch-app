# Run 2: edge-to-edge fixes, pull-to-refresh, API 35 swipe bisection

Source: `debug/e2e-check` @ 150553d, run 40106454005 (attempt 1), 2026-10-24T15:37:24Z.
Emulators: google_apis x86_64, pixel_6 profile. Jobs: e2e on API 29/35 (09278c0), swipe-only on API 35 per app version and GPU mode.

## e2e-api29

```
APK: head @ 150553d (ВРЕМЕННО: втори кръг — дърпане за опреснява) size 4810093 bytes; mode e2e
emulator: -gpu swiftshader_indirect -show-kernel; host: 4 cpu, 15989 MB RAM
gles: emulation  GLES: Google (Google Inc.), Android Emulator OpenGL ES Translator (Google SwiftShader), OpenGL ES 3.0 (OpenGL ES 3.0 SwiftShader 4.0.0.1)
APK entries:   8727066                     1031 files
screen 1080x2400
## 0. Device
android 10 sdk 29; Physical size: 1080x2400; Physical density: 420
NAV gestural: navigation_mode=2; overlays: [ ] com.android.internal.systemui.navbar.gestural_wide_back [ ] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.gestural_extra_wide_back [x] com.android.internal.systemui.navbar.gestural [ ] com.android.internal.systemui.navbar.gestural_narrow_back 
cutout before: Cutout=WmDisplayCutout{DisplayCutout{insets=Rect(0, 0 - 0, 0) boundingRect={Bounds=[Rect(0, 0 
cutout overlays: [ ] com.android.internal.display.cutout.emulation.corner [ ] com.android.internal.display.cutout.emulation.double [ ] com.android.internal.display.cutout.emulation.tall [ ] com.android.internal.display.cutout.emulation.emu01 
CUTOUT enabled: tall; overlays: [ ] com.android.internal.display.cutout.emulation.corner [ ] com.android.internal.display.cutout.emulation.double [x] com.android.internal.display.cutout.emulation.tall [ ] com.android.internal.display.cutout.emulation.emu01 
cutout after: Cutout=WmDisplayCutout{DisplayCutout{insets=Rect(0, 126 - 0, 0) boundingRect={Bounds=[Rect(0, 0 
screen 1080x2400
## 1. Fresh install: splash, intro, first onboarding page (animations ON)
intro video: 645198 bytes, frames: 17
WIN onboarding: Window{4dbae63 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x700  mFrame=[0,0][1080,2400]
## 2. Skip onboarding; Home light
WIN home-light: Window{4dbae63 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][1080,2400]
WIN catalog-light: Window{4dbae63 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][1080,2400]
## 3. System dark (app theme = System)
NIGHT yes via Settings: after 1 polls: Night mode: yes
NIGHT yes: Night mode: yes ; ui_night_mode=2
WIN home-dark: Window{4dbae63 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x700  mFrame=[0,0][1080,2400]
WIN catalog-dark: Window{4dbae63 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x700  mFrame=[0,0][1080,2400]
## 4. App Light in a dark system
THEME -> =Light|=Светла (settings now shows: Light)
WIN home-applight-sysdark: Window{4dbae63 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][1080,2400]
## 4b. App Dark in a light system
NIGHT no via Settings: after 1 polls: Night mode: no
NIGHT no: Night mode: no ; ui_night_mode=1
THEME -> =Dark|=Тъмна (settings now shows: Dark)
WIN home-appdark-syslight: Window{4dbae63 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x700  mFrame=[0,0][1080,2400]
THEME -> =System default|=Както системата (settings now shows: System default)
## 5. 3-button navigation
NAV threebutton: navigation_mode=0; overlays: [ ] com.android.internal.systemui.navbar.gestural_wide_back [x] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.gestural_extra_wide_back [ ] com.android.internal.systemui.navbar.gestural [ ] com.android.internal.systemui.navbar.gestural_narrow_back 
WIN home-3btn-light: Window{4dbae63 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][1080,2400]
NIGHT yes via Settings: after 1 polls: Night mode: yes
NIGHT yes: Night mode: yes ; ui_night_mode=2
WIN home-3btn-dark: Window{4dbae63 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x700  mFrame=[0,0][1080,2400]
NIGHT no via Settings: after 1 polls: Night mode: no
NIGHT no: Night mode: no ; ui_night_mode=1
## 6. Back to gesture navigation
NAV gestural: navigation_mode=2; overlays: [ ] com.android.internal.systemui.navbar.gestural_wide_back [ ] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.gestural_extra_wide_back [x] com.android.internal.systemui.navbar.gestural [ ] com.android.internal.systemui.navbar.gestural_narrow_back 
WIN home-gesture-light: Window{4dbae63 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][1080,2400]
## 7. Landscape with 3-button navigation (cutout)
NAV threebutton: navigation_mode=0; overlays: [ ] com.android.internal.systemui.navbar.gestural_wide_back [x] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.gestural_extra_wide_back [ ] com.android.internal.systemui.navbar.gestural [ ] com.android.internal.systemui.navbar.gestural_narrow_back 
ROTATION 1: mCurrentRotation=ROTATION_90 cur=2400x1080
WIN land90-home: Window{4dbae63 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight land  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][2400,1080]
ROTATION 3: mCurrentRotation=ROTATION_270 cur=2400x1080
WIN land270-catalog: Window{4dbae63 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight land  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][2400,1080]
NIGHT yes via Settings: after 1 polls: Night mode: yes
NIGHT yes: Night mode: yes ; ui_night_mode=2
ROTATION 1: mCurrentRotation=ROTATION_90 cur=2400x1080
NIGHT no via Settings: after 1 polls: Night mode: no
NIGHT no: Night mode: no ; ui_night_mode=1
ROTATION 0: mCurrentRotation=ROTATION_0 cur=1080x2400
## 8. Pull-to-refresh on Home (leaf indicator)
PTR ptr-home-light: video 364658 bytes, 26 frames
## 9. Scrolled content under the bars (3-button, then gestures)
NAV gestural: navigation_mode=2; overlays: [ ] com.android.internal.systemui.navbar.gestural_wide_back [ ] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.gestural_extra_wide_back [x] com.android.internal.systemui.navbar.gestural [ ] com.android.internal.systemui.navbar.gestural_narrow_back 
NIGHT yes via Settings: after 1 polls: Night mode: yes
NIGHT yes: Night mode: yes ; ui_night_mode=2
PTR ptr-home-dark: video 525848 bytes, 25 frames
NIGHT no via Settings: after 1 polls: Night mode: no
NIGHT no: Night mode: no ; ui_night_mode=1
## COMPLETE
## logcat findings
app pids seen: ['6701']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 3
   skipped-frames events=3 max=84 total=153
== Davey: 4
   davey events=4 max=1545ms median=1240ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## e2e-api35

```
APK: head @ 150553d (ВРЕМЕННО: втори кръг — дърпане за опреснява) size 4810093 bytes; mode e2e
emulator: -gpu swiftshader_indirect -show-kernel; host: 4 cpu, 15989 MB RAM
gles: emulation  GLES: Google (Google Inc.), Android Emulator OpenGL ES Translator (Google SwiftShader), OpenGL ES 3.0 (OpenGL ES 3.0 SwiftShader 4.0.0.1)
APK entries:   8727066                     1031 files
screen 1080x2400
## 0. Device
android 15 sdk 35; Physical size: 1080x2400; Physical density: 420
NAV gestural: navigation_mode=2; overlays: [ ] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.transparent [x] com.android.internal.systemui.navbar.gestural 
cutout before: Cutout=DisplayCutout{insets=Rect(0, 128 - 0, 0) waterfall=Insets{left=0, top=0, 
cutout overlays: [ ] com.android.internal.display.cutout.emulation.corner [ ] com.android.internal.display.cutout.emulation.double [ ] com.android.internal.display.cutout.emulation.hole [ ] com.android.internal.display.cutout.emulation.tall [ ] com.android.internal.display.cutout.emulation.emu01 [ ] com.android.internal.display.cutout.emulation.waterfall 
CUTOUT: native cutout present, no emulation overlay
screen 1080x2400
## 1. Fresh install: splash, intro, first onboarding page (animations ON)
intro video: 336768 bytes, frames: 32
WIN onboarding: Window{214ac46 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
## 2. Skip onboarding; Home light
WIN home-light: Window{214ac46 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
WIN catalog-light: Window{214ac46 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
## 3. System dark (app theme = System)
NIGHT yes: Night mode: yes ; ui_night_mode=2
WIN home-dark: Window{214ac46 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
WIN catalog-dark: Window{214ac46 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
## 4. App Light in a dark system
THEME -> =Light|=Светла (settings now shows: Light)
WIN home-applight-sysdark: Window{214ac46 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
## 4b. App Dark in a light system
NIGHT no: Night mode: no ; ui_night_mode=1
THEME -> =Dark|=Тъмна (settings now shows: Dark)
WIN home-appdark-syslight: Window{214ac46 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
THEME -> =System default|=Както системата (settings now shows: System default)
## 5. 3-button navigation
NAV threebutton: navigation_mode=0; overlays: [x] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.transparent [ ] com.android.internal.systemui.navbar.gestural 
WIN home-3btn-light: Window{7dbd6f2 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
NIGHT yes: Night mode: yes ; ui_night_mode=2
WIN home-3btn-dark: Window{7dbd6f2 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
NIGHT no: Night mode: no ; ui_night_mode=1
## 6. Back to gesture navigation
NAV gestural: navigation_mode=2; overlays: [ ] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.transparent [x] com.android.internal.systemui.navbar.gestural 
WIN home-gesture-light: Window{2a8b0aa u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
## 7. Landscape with 3-button navigation (cutout)
NAV threebutton: navigation_mode=0; overlays: [x] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.transparent [ ] com.android.internal.systemui.navbar.gestural 
ROTATION 1: mRotation=ROTATION_90} cur=2400x1080
WIN land90-home: Window{6ba15c2 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight land  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
ROTATION 3: mRotation=ROTATION_270} cur=2400x1080
WIN land270-catalog: Window{6ba15c2 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight land  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
NIGHT yes: Night mode: yes ; ui_night_mode=2
ROTATION 1: mRotation=ROTATION_90} cur=2400x1080
NIGHT no: Night mode: no ; ui_night_mode=1
ROTATION 0: mRotation=ROTATION_0} cur=1080x2400
## 8. Pull-to-refresh on Home (leaf indicator)
PTR ptr-home-light: video 341299 bytes, 25 frames
## 9. Scrolled content under the bars (3-button, then gestures)
NAV gestural: navigation_mode=2; overlays: [ ] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.transparent [x] com.android.internal.systemui.navbar.gestural 
NIGHT yes: Night mode: yes ; ui_night_mode=2
PTR ptr-home-dark: video 389420 bytes, 26 frames
NIGHT no: Night mode: no ; ui_night_mode=1
## COMPLETE
## logcat findings
app pids seen: ['4340']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=65 total=229
== Davey: 8
   davey events=8 max=1682ms median=1180ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## swipe35-e2e-6278bd9

```
APK: e2e-6278bd9 @ 6278bd9 (От край до край без оттеглените API на Android 15) size 4810045 bytes; mode swipe
emulator: -gpu swiftshader_indirect -show-kernel; host: 4 cpu, 15989 MB RAM
gles: emulation  GLES: Google (Google Inc.), Android Emulator OpenGL ES Translator (Google SwiftShader), OpenGL ES 3.0 (OpenGL ES 3.0 SwiftShader 4.0.0.1)
APK entries:   8725896                     1031 files
screen 1080x2400
## S0. Device
android 15 sdk 35
screen 1080x2400
## S1. First launch, skip onboarding, Home
## S2. Swipes on More
SWIPE more #1 ok
SWIPE more #2 ok
SWIPE more #3 ok
## S3. Swipes on Catalogue
SWIPE catalog #1 ok
SWIPE catalog #2 ok
SWIPE catalog #3 ok
## S4. Swipes on Home
!!! EMULATOR GONE (at: home swipe 1)
host evidence: 1 oom lines; qemu procs: 0; last mon: 15:29:35 used/avail_MB=1139/14849 swap_MB=0 qemu_rss_MB=0 load=1.67
## logcat findings
app pids seen: ['3928']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 3
   skipped-frames events=3 max=36 total=106
== Davey: 7
   davey events=7 max=1303ms median=833ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## swipe35-head-gpu-guest

```
APK: head @ 150553d (ВРЕМЕННО: втори кръг — дърпане за опреснява) size 4810093 bytes; mode swipe
emulator: -gpu guest -show-kernel; host: 4 cpu, 15989 MB RAM
gles: emulation  GLES: Google (Google Inc.), Android Emulator OpenGL ES Translator (Google SwiftShader), OpenGL ES 3.0 (OpenGL ES 3.0 SwiftShader 4.0.0.1)
APK entries:   8727066                     1031 files
screen 1080x2400
## S0. Device
android 15 sdk 35
screen 1080x2400
## S1. First launch, skip onboarding, Home
## S2. Swipes on More
SWIPE more #1 ok
SWIPE more #2 ok
SWIPE more #3 ok
## S3. Swipes on Catalogue
SWIPE catalog #1 ok
SWIPE catalog #2 ok
SWIPE catalog #3 ok
## S4. Swipes on Home
SWIPE home #1 ok
!!! EMULATOR GONE (at: home swipe 2)
host evidence: 1 oom lines; qemu procs: 0; last mon: 15:29:41 used/avail_MB=1177/14812 swap_MB=0 qemu_rss_MB=0 load=2.03
## logcat findings
app pids seen: ['3778']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=71 total=215
== Davey: 10
   davey events=10 max=1350ms median=879ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## swipe35-head

```
APK: head @ 150553d (ВРЕМЕННО: втори кръг — дърпане за опреснява) size 4810093 bytes; mode swipe
emulator: -gpu swiftshader_indirect -show-kernel; host: 4 cpu, 15989 MB RAM
gles: emulation  GLES: Google (Google Inc.), Android Emulator OpenGL ES Translator (Google SwiftShader), OpenGL ES 3.0 (OpenGL ES 3.0 SwiftShader 4.0.0.1)
APK entries:   8727066                     1031 files
screen 1080x2400
## S0. Device
android 15 sdk 35
screen 1080x2400
## S1. First launch, skip onboarding, Home
## S2. Swipes on More
SWIPE more #1 ok
SWIPE more #2 ok
SWIPE more #3 ok
## S3. Swipes on Catalogue
SWIPE catalog #1 ok
SWIPE catalog #2 ok
SWIPE catalog #3 ok
## S4. Swipes on Home
SWIPE home #1 ok
SWIPE home #2 ok
SWIPE home #3 ok
SWIPE home #4 ok
SWIPE home #5 ok
SWIPE home #6 ok
!!! EMULATOR GONE (at: home swipe 7)
host evidence: 1 oom lines; qemu procs: 0; last mon: 15:29:51 used/avail_MB=1271/14718 swap_MB=0 qemu_rss_MB=0 load=1.79
## logcat findings
app pids seen: ['3729']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=43 total=157
== Davey: 8
   davey events=8 max=1347ms median=930ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## swipe35-v1.2.23

```
APK: v1.2.23 @ ce4af47 (Издаване: бележките отразяват main като клон �) size 4890407 bytes; mode swipe
emulator: -gpu swiftshader_indirect -show-kernel; host: 4 cpu, 15989 MB RAM
gles: emulation  GLES: Google (Google Inc.), Android Emulator OpenGL ES Translator (Google SwiftShader), OpenGL ES 3.0 (OpenGL ES 3.0 SwiftShader 4.0.0.1)
APK entries:   8782305                     1038 files
screen 1080x2400
## S0. Device
android 15 sdk 35
screen 1080x2400
## S1. First launch, skip onboarding, Home
## S2. Swipes on More
SWIPE more #1 ok
SWIPE more #2 ok
SWIPE more #3 ok
## S3. Swipes on Catalogue
SWIPE catalog #1 ok
SWIPE catalog #2 ok
SWIPE catalog #3 ok
## S4. Swipes on Home
SWIPE home #1 ok
!!! EMULATOR GONE (at: home swipe 2)
host evidence: 1 oom lines; qemu procs: 0; last mon: 15:29:51 used/avail_MB=1119/14869 swap_MB=0 qemu_rss_MB=0 load=1.90
## logcat findings
app pids seen: ['3833']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=54 total=54
== Davey: 4
   davey events=4 max=1150ms median=982ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## swipe35-v1.2.24

```
APK: v1.2.24 @ f646270 (Merge remote-tracking branch 'origin/wave11/perf' into claude/charming-rubin-3lz) size 4806181 bytes; mode swipe
emulator: -gpu swiftshader_indirect -show-kernel; host: 4 cpu, 15989 MB RAM
gles: emulation  GLES: Google (Google Inc.), Android Emulator OpenGL ES Translator (Google SwiftShader), OpenGL ES 3.0 (OpenGL ES 3.0 SwiftShader 4.0.0.1)
APK entries:   8722619                     1031 files
screen 1080x2400
## S0. Device
android 15 sdk 35
screen 1080x2400
## S1. First launch, skip onboarding, Home
## S2. Swipes on More
SWIPE more #1 ok
SWIPE more #2 ok
SWIPE more #3 ok
## S3. Swipes on Catalogue
SWIPE catalog #1 ok
SWIPE catalog #2 ok
SWIPE catalog #3 ok
## S4. Swipes on Home
SWIPE home #1 ok
SWIPE home #2 ok
SWIPE home #3 ok
SWIPE home #4 ok
SWIPE home #5 ok
!!! EMULATOR GONE (at: home swipe 6)
host evidence: 1 oom lines; qemu procs: 0; last mon: 15:29:57 used/avail_MB=1321/14668 swap_MB=0 qemu_rss_MB=0 load=1.51
## logcat findings
app pids seen: ['3876']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=54 total=151
== Davey: 4
   davey events=4 max=1399ms median=1034ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Screenshots

### e2e-api29/shots

- [e2e-api29/shots/01-intro-01.png](e2e-api29/shots/01-intro-01.png)
- [e2e-api29/shots/01-intro-02.png](e2e-api29/shots/01-intro-02.png)
- [e2e-api29/shots/01-intro-03.png](e2e-api29/shots/01-intro-03.png)
- [e2e-api29/shots/01-intro-04.png](e2e-api29/shots/01-intro-04.png)
- [e2e-api29/shots/01-intro-05.png](e2e-api29/shots/01-intro-05.png)
- [e2e-api29/shots/01-intro-06.png](e2e-api29/shots/01-intro-06.png)
- [e2e-api29/shots/01-intro-07.png](e2e-api29/shots/01-intro-07.png)
- [e2e-api29/shots/01-intro-08.png](e2e-api29/shots/01-intro-08.png)
- [e2e-api29/shots/01-intro-09.png](e2e-api29/shots/01-intro-09.png)
- [e2e-api29/shots/01-intro-10.png](e2e-api29/shots/01-intro-10.png)
- [e2e-api29/shots/01-intro-11.png](e2e-api29/shots/01-intro-11.png)
- [e2e-api29/shots/01-intro-12.png](e2e-api29/shots/01-intro-12.png)
- [e2e-api29/shots/01-intro-13.png](e2e-api29/shots/01-intro-13.png)
- [e2e-api29/shots/01-intro-14.png](e2e-api29/shots/01-intro-14.png)
- [e2e-api29/shots/01-intro-15.png](e2e-api29/shots/01-intro-15.png)
- [e2e-api29/shots/01-intro-16.png](e2e-api29/shots/01-intro-16.png)
- [e2e-api29/shots/01-intro-17.png](e2e-api29/shots/01-intro-17.png)
- [e2e-api29/shots/02-onboarding-1.png](e2e-api29/shots/02-onboarding-1.png)
- [e2e-api29/shots/03-home-light.png](e2e-api29/shots/03-home-light.png)
- [e2e-api29/shots/03c-catalog-light.png](e2e-api29/shots/03c-catalog-light.png)
- [e2e-api29/shots/04-home-dark.png](e2e-api29/shots/04-home-dark.png)
- [e2e-api29/shots/05-catalog-dark.png](e2e-api29/shots/05-catalog-dark.png)
- [e2e-api29/shots/06-home-applight-sysdark.png](e2e-api29/shots/06-home-applight-sysdark.png)
- [e2e-api29/shots/06a-settings-applight-sysdark.png](e2e-api29/shots/06a-settings-applight-sysdark.png)
- [e2e-api29/shots/06b-catalog-applight-sysdark.png](e2e-api29/shots/06b-catalog-applight-sysdark.png)
- [e2e-api29/shots/07-home-appdark-syslight.png](e2e-api29/shots/07-home-appdark-syslight.png)
- [e2e-api29/shots/07a-settings-appdark-syslight.png](e2e-api29/shots/07a-settings-appdark-syslight.png)
- [e2e-api29/shots/07b-catalog-appdark-syslight.png](e2e-api29/shots/07b-catalog-appdark-syslight.png)
- [e2e-api29/shots/08-home-3btn-light.png](e2e-api29/shots/08-home-3btn-light.png)
- [e2e-api29/shots/08c-catalog-3btn-light.png](e2e-api29/shots/08c-catalog-3btn-light.png)
- [e2e-api29/shots/09-home-3btn-dark.png](e2e-api29/shots/09-home-3btn-dark.png)
- [e2e-api29/shots/09c-catalog-3btn-dark.png](e2e-api29/shots/09c-catalog-3btn-dark.png)
- [e2e-api29/shots/10-home-gesture-light.png](e2e-api29/shots/10-home-gesture-light.png)
- [e2e-api29/shots/11-land90-home.png](e2e-api29/shots/11-land90-home.png)
- [e2e-api29/shots/12-land90-catalog.png](e2e-api29/shots/12-land90-catalog.png)
- [e2e-api29/shots/13-land270-catalog.png](e2e-api29/shots/13-land270-catalog.png)
- [e2e-api29/shots/14-land270-home.png](e2e-api29/shots/14-land270-home.png)
- [e2e-api29/shots/15-land270-home-dark.png](e2e-api29/shots/15-land270-home-dark.png)
- [e2e-api29/shots/16-land90-home-dark.png](e2e-api29/shots/16-land90-home-dark.png)
- [e2e-api29/shots/17-home-3btn-light-scrolled.png](e2e-api29/shots/17-home-3btn-light-scrolled.png)
- [e2e-api29/shots/18-catalog-3btn-light-scrolled.png](e2e-api29/shots/18-catalog-3btn-light-scrolled.png)
- [e2e-api29/shots/19-home-gesture-light-scrolled.png](e2e-api29/shots/19-home-gesture-light-scrolled.png)
- [e2e-api29/shots/20-home-gesture-dark-scrolled.png](e2e-api29/shots/20-home-gesture-dark-scrolled.png)

### e2e-api29/shots/debug

- [e2e-api29/shots/debug/night-no-1-a-before.png](e2e-api29/shots/debug/night-no-1-a-before.png)
- [e2e-api29/shots/debug/night-no-1-b-after.png](e2e-api29/shots/debug/night-no-1-b-after.png)
- [e2e-api29/shots/debug/night-no-3-a-before.png](e2e-api29/shots/debug/night-no-3-a-before.png)
- [e2e-api29/shots/debug/night-no-3-b-after.png](e2e-api29/shots/debug/night-no-3-b-after.png)
- [e2e-api29/shots/debug/night-no-5-a-before.png](e2e-api29/shots/debug/night-no-5-a-before.png)
- [e2e-api29/shots/debug/night-no-5-b-after.png](e2e-api29/shots/debug/night-no-5-b-after.png)
- [e2e-api29/shots/debug/night-no-7-a-before.png](e2e-api29/shots/debug/night-no-7-a-before.png)
- [e2e-api29/shots/debug/night-no-7-b-after.png](e2e-api29/shots/debug/night-no-7-b-after.png)
- [e2e-api29/shots/debug/night-yes-0-a-before.png](e2e-api29/shots/debug/night-yes-0-a-before.png)
- [e2e-api29/shots/debug/night-yes-0-b-after.png](e2e-api29/shots/debug/night-yes-0-b-after.png)
- [e2e-api29/shots/debug/night-yes-2-a-before.png](e2e-api29/shots/debug/night-yes-2-a-before.png)
- [e2e-api29/shots/debug/night-yes-2-b-after.png](e2e-api29/shots/debug/night-yes-2-b-after.png)
- [e2e-api29/shots/debug/night-yes-4-a-before.png](e2e-api29/shots/debug/night-yes-4-a-before.png)
- [e2e-api29/shots/debug/night-yes-4-b-after.png](e2e-api29/shots/debug/night-yes-4-b-after.png)
- [e2e-api29/shots/debug/night-yes-6-a-before.png](e2e-api29/shots/debug/night-yes-6-a-before.png)
- [e2e-api29/shots/debug/night-yes-6-b-after.png](e2e-api29/shots/debug/night-yes-6-b-after.png)

### e2e-api29/shots/ptr-home-dark

- [e2e-api29/shots/ptr-home-dark/after-release.png](e2e-api29/shots/ptr-home-dark/after-release.png)
- [e2e-api29/shots/ptr-home-dark/f-01.png](e2e-api29/shots/ptr-home-dark/f-01.png)
- [e2e-api29/shots/ptr-home-dark/f-02.png](e2e-api29/shots/ptr-home-dark/f-02.png)
- [e2e-api29/shots/ptr-home-dark/f-03.png](e2e-api29/shots/ptr-home-dark/f-03.png)
- [e2e-api29/shots/ptr-home-dark/f-04.png](e2e-api29/shots/ptr-home-dark/f-04.png)
- [e2e-api29/shots/ptr-home-dark/f-05.png](e2e-api29/shots/ptr-home-dark/f-05.png)
- [e2e-api29/shots/ptr-home-dark/f-06.png](e2e-api29/shots/ptr-home-dark/f-06.png)
- [e2e-api29/shots/ptr-home-dark/f-07.png](e2e-api29/shots/ptr-home-dark/f-07.png)
- [e2e-api29/shots/ptr-home-dark/f-08.png](e2e-api29/shots/ptr-home-dark/f-08.png)
- [e2e-api29/shots/ptr-home-dark/f-09.png](e2e-api29/shots/ptr-home-dark/f-09.png)
- [e2e-api29/shots/ptr-home-dark/f-10.png](e2e-api29/shots/ptr-home-dark/f-10.png)
- [e2e-api29/shots/ptr-home-dark/f-11.png](e2e-api29/shots/ptr-home-dark/f-11.png)
- [e2e-api29/shots/ptr-home-dark/f-12.png](e2e-api29/shots/ptr-home-dark/f-12.png)
- [e2e-api29/shots/ptr-home-dark/f-13.png](e2e-api29/shots/ptr-home-dark/f-13.png)
- [e2e-api29/shots/ptr-home-dark/f-14.png](e2e-api29/shots/ptr-home-dark/f-14.png)
- [e2e-api29/shots/ptr-home-dark/f-15.png](e2e-api29/shots/ptr-home-dark/f-15.png)
- [e2e-api29/shots/ptr-home-dark/f-16.png](e2e-api29/shots/ptr-home-dark/f-16.png)
- [e2e-api29/shots/ptr-home-dark/f-17.png](e2e-api29/shots/ptr-home-dark/f-17.png)
- [e2e-api29/shots/ptr-home-dark/f-18.png](e2e-api29/shots/ptr-home-dark/f-18.png)
- [e2e-api29/shots/ptr-home-dark/f-19.png](e2e-api29/shots/ptr-home-dark/f-19.png)
- [e2e-api29/shots/ptr-home-dark/f-20.png](e2e-api29/shots/ptr-home-dark/f-20.png)
- [e2e-api29/shots/ptr-home-dark/f-21.png](e2e-api29/shots/ptr-home-dark/f-21.png)
- [e2e-api29/shots/ptr-home-dark/f-22.png](e2e-api29/shots/ptr-home-dark/f-22.png)
- [e2e-api29/shots/ptr-home-dark/f-23.png](e2e-api29/shots/ptr-home-dark/f-23.png)
- [e2e-api29/shots/ptr-home-dark/f-24.png](e2e-api29/shots/ptr-home-dark/f-24.png)
- [e2e-api29/shots/ptr-home-dark/f-25.png](e2e-api29/shots/ptr-home-dark/f-25.png)
- [e2e-api29/shots/ptr-home-dark/mid-pull.png](e2e-api29/shots/ptr-home-dark/mid-pull.png)
- [e2e-api29/shots/ptr-home-dark/spinning.png](e2e-api29/shots/ptr-home-dark/spinning.png)

### e2e-api29/shots/ptr-home-light

- [e2e-api29/shots/ptr-home-light/after-release.png](e2e-api29/shots/ptr-home-light/after-release.png)
- [e2e-api29/shots/ptr-home-light/f-01.png](e2e-api29/shots/ptr-home-light/f-01.png)
- [e2e-api29/shots/ptr-home-light/f-02.png](e2e-api29/shots/ptr-home-light/f-02.png)
- [e2e-api29/shots/ptr-home-light/f-03.png](e2e-api29/shots/ptr-home-light/f-03.png)
- [e2e-api29/shots/ptr-home-light/f-04.png](e2e-api29/shots/ptr-home-light/f-04.png)
- [e2e-api29/shots/ptr-home-light/f-05.png](e2e-api29/shots/ptr-home-light/f-05.png)
- [e2e-api29/shots/ptr-home-light/f-06.png](e2e-api29/shots/ptr-home-light/f-06.png)
- [e2e-api29/shots/ptr-home-light/f-07.png](e2e-api29/shots/ptr-home-light/f-07.png)
- [e2e-api29/shots/ptr-home-light/f-08.png](e2e-api29/shots/ptr-home-light/f-08.png)
- [e2e-api29/shots/ptr-home-light/f-09.png](e2e-api29/shots/ptr-home-light/f-09.png)
- [e2e-api29/shots/ptr-home-light/f-10.png](e2e-api29/shots/ptr-home-light/f-10.png)
- [e2e-api29/shots/ptr-home-light/f-11.png](e2e-api29/shots/ptr-home-light/f-11.png)
- [e2e-api29/shots/ptr-home-light/f-12.png](e2e-api29/shots/ptr-home-light/f-12.png)
- [e2e-api29/shots/ptr-home-light/f-13.png](e2e-api29/shots/ptr-home-light/f-13.png)
- [e2e-api29/shots/ptr-home-light/f-14.png](e2e-api29/shots/ptr-home-light/f-14.png)
- [e2e-api29/shots/ptr-home-light/f-15.png](e2e-api29/shots/ptr-home-light/f-15.png)
- [e2e-api29/shots/ptr-home-light/f-16.png](e2e-api29/shots/ptr-home-light/f-16.png)
- [e2e-api29/shots/ptr-home-light/f-17.png](e2e-api29/shots/ptr-home-light/f-17.png)
- [e2e-api29/shots/ptr-home-light/f-18.png](e2e-api29/shots/ptr-home-light/f-18.png)
- [e2e-api29/shots/ptr-home-light/f-19.png](e2e-api29/shots/ptr-home-light/f-19.png)
- [e2e-api29/shots/ptr-home-light/f-20.png](e2e-api29/shots/ptr-home-light/f-20.png)
- [e2e-api29/shots/ptr-home-light/f-21.png](e2e-api29/shots/ptr-home-light/f-21.png)
- [e2e-api29/shots/ptr-home-light/f-22.png](e2e-api29/shots/ptr-home-light/f-22.png)
- [e2e-api29/shots/ptr-home-light/f-23.png](e2e-api29/shots/ptr-home-light/f-23.png)
- [e2e-api29/shots/ptr-home-light/f-24.png](e2e-api29/shots/ptr-home-light/f-24.png)
- [e2e-api29/shots/ptr-home-light/f-25.png](e2e-api29/shots/ptr-home-light/f-25.png)
- [e2e-api29/shots/ptr-home-light/f-26.png](e2e-api29/shots/ptr-home-light/f-26.png)
- [e2e-api29/shots/ptr-home-light/mid-pull.png](e2e-api29/shots/ptr-home-light/mid-pull.png)
- [e2e-api29/shots/ptr-home-light/spinning.png](e2e-api29/shots/ptr-home-light/spinning.png)

### e2e-api35/shots

- [e2e-api35/shots/01-intro-01.png](e2e-api35/shots/01-intro-01.png)
- [e2e-api35/shots/01-intro-02.png](e2e-api35/shots/01-intro-02.png)
- [e2e-api35/shots/01-intro-03.png](e2e-api35/shots/01-intro-03.png)
- [e2e-api35/shots/01-intro-04.png](e2e-api35/shots/01-intro-04.png)
- [e2e-api35/shots/01-intro-05.png](e2e-api35/shots/01-intro-05.png)
- [e2e-api35/shots/01-intro-06.png](e2e-api35/shots/01-intro-06.png)
- [e2e-api35/shots/01-intro-07.png](e2e-api35/shots/01-intro-07.png)
- [e2e-api35/shots/01-intro-08.png](e2e-api35/shots/01-intro-08.png)
- [e2e-api35/shots/01-intro-09.png](e2e-api35/shots/01-intro-09.png)
- [e2e-api35/shots/01-intro-10.png](e2e-api35/shots/01-intro-10.png)
- [e2e-api35/shots/01-intro-11.png](e2e-api35/shots/01-intro-11.png)
- [e2e-api35/shots/01-intro-12.png](e2e-api35/shots/01-intro-12.png)
- [e2e-api35/shots/01-intro-13.png](e2e-api35/shots/01-intro-13.png)
- [e2e-api35/shots/01-intro-14.png](e2e-api35/shots/01-intro-14.png)
- [e2e-api35/shots/01-intro-15.png](e2e-api35/shots/01-intro-15.png)
- [e2e-api35/shots/01-intro-16.png](e2e-api35/shots/01-intro-16.png)
- [e2e-api35/shots/01-intro-17.png](e2e-api35/shots/01-intro-17.png)
- [e2e-api35/shots/01-intro-18.png](e2e-api35/shots/01-intro-18.png)
- [e2e-api35/shots/01-intro-19.png](e2e-api35/shots/01-intro-19.png)
- [e2e-api35/shots/01-intro-20.png](e2e-api35/shots/01-intro-20.png)
- [e2e-api35/shots/01-intro-21.png](e2e-api35/shots/01-intro-21.png)
- [e2e-api35/shots/01-intro-22.png](e2e-api35/shots/01-intro-22.png)
- [e2e-api35/shots/01-intro-23.png](e2e-api35/shots/01-intro-23.png)
- [e2e-api35/shots/01-intro-24.png](e2e-api35/shots/01-intro-24.png)
- [e2e-api35/shots/01-intro-25.png](e2e-api35/shots/01-intro-25.png)
- [e2e-api35/shots/01-intro-26.png](e2e-api35/shots/01-intro-26.png)
- [e2e-api35/shots/01-intro-27.png](e2e-api35/shots/01-intro-27.png)
- [e2e-api35/shots/01-intro-28.png](e2e-api35/shots/01-intro-28.png)
- [e2e-api35/shots/01-intro-29.png](e2e-api35/shots/01-intro-29.png)
- [e2e-api35/shots/01-intro-30.png](e2e-api35/shots/01-intro-30.png)
- [e2e-api35/shots/01-intro-31.png](e2e-api35/shots/01-intro-31.png)
- [e2e-api35/shots/01-intro-32.png](e2e-api35/shots/01-intro-32.png)
- [e2e-api35/shots/02-onboarding-1.png](e2e-api35/shots/02-onboarding-1.png)
- [e2e-api35/shots/03-home-light.png](e2e-api35/shots/03-home-light.png)
- [e2e-api35/shots/03c-catalog-light.png](e2e-api35/shots/03c-catalog-light.png)
- [e2e-api35/shots/04-home-dark.png](e2e-api35/shots/04-home-dark.png)
- [e2e-api35/shots/05-catalog-dark.png](e2e-api35/shots/05-catalog-dark.png)
- [e2e-api35/shots/06-home-applight-sysdark.png](e2e-api35/shots/06-home-applight-sysdark.png)
- [e2e-api35/shots/06a-settings-applight-sysdark.png](e2e-api35/shots/06a-settings-applight-sysdark.png)
- [e2e-api35/shots/06b-catalog-applight-sysdark.png](e2e-api35/shots/06b-catalog-applight-sysdark.png)
- [e2e-api35/shots/07-home-appdark-syslight.png](e2e-api35/shots/07-home-appdark-syslight.png)
- [e2e-api35/shots/07a-settings-appdark-syslight.png](e2e-api35/shots/07a-settings-appdark-syslight.png)
- [e2e-api35/shots/07b-catalog-appdark-syslight.png](e2e-api35/shots/07b-catalog-appdark-syslight.png)
- [e2e-api35/shots/08-home-3btn-light.png](e2e-api35/shots/08-home-3btn-light.png)
- [e2e-api35/shots/08c-catalog-3btn-light.png](e2e-api35/shots/08c-catalog-3btn-light.png)
- [e2e-api35/shots/09-home-3btn-dark.png](e2e-api35/shots/09-home-3btn-dark.png)
- [e2e-api35/shots/09c-catalog-3btn-dark.png](e2e-api35/shots/09c-catalog-3btn-dark.png)
- [e2e-api35/shots/10-home-gesture-light.png](e2e-api35/shots/10-home-gesture-light.png)
- [e2e-api35/shots/11-land90-home.png](e2e-api35/shots/11-land90-home.png)
- [e2e-api35/shots/12-land90-catalog.png](e2e-api35/shots/12-land90-catalog.png)
- [e2e-api35/shots/13-land270-catalog.png](e2e-api35/shots/13-land270-catalog.png)
- [e2e-api35/shots/14-land270-home.png](e2e-api35/shots/14-land270-home.png)
- [e2e-api35/shots/15-land270-home-dark.png](e2e-api35/shots/15-land270-home-dark.png)
- [e2e-api35/shots/16-land90-home-dark.png](e2e-api35/shots/16-land90-home-dark.png)
- [e2e-api35/shots/17-home-3btn-light-scrolled.png](e2e-api35/shots/17-home-3btn-light-scrolled.png)
- [e2e-api35/shots/18-catalog-3btn-light-scrolled.png](e2e-api35/shots/18-catalog-3btn-light-scrolled.png)
- [e2e-api35/shots/19-home-gesture-light-scrolled.png](e2e-api35/shots/19-home-gesture-light-scrolled.png)
- [e2e-api35/shots/20-home-gesture-dark-scrolled.png](e2e-api35/shots/20-home-gesture-dark-scrolled.png)

### e2e-api35/shots/ptr-home-dark

- [e2e-api35/shots/ptr-home-dark/after-release.png](e2e-api35/shots/ptr-home-dark/after-release.png)
- [e2e-api35/shots/ptr-home-dark/f-01.png](e2e-api35/shots/ptr-home-dark/f-01.png)
- [e2e-api35/shots/ptr-home-dark/f-02.png](e2e-api35/shots/ptr-home-dark/f-02.png)
- [e2e-api35/shots/ptr-home-dark/f-03.png](e2e-api35/shots/ptr-home-dark/f-03.png)
- [e2e-api35/shots/ptr-home-dark/f-04.png](e2e-api35/shots/ptr-home-dark/f-04.png)
- [e2e-api35/shots/ptr-home-dark/f-05.png](e2e-api35/shots/ptr-home-dark/f-05.png)
- [e2e-api35/shots/ptr-home-dark/f-06.png](e2e-api35/shots/ptr-home-dark/f-06.png)
- [e2e-api35/shots/ptr-home-dark/f-07.png](e2e-api35/shots/ptr-home-dark/f-07.png)
- [e2e-api35/shots/ptr-home-dark/f-08.png](e2e-api35/shots/ptr-home-dark/f-08.png)
- [e2e-api35/shots/ptr-home-dark/f-09.png](e2e-api35/shots/ptr-home-dark/f-09.png)
- [e2e-api35/shots/ptr-home-dark/f-10.png](e2e-api35/shots/ptr-home-dark/f-10.png)
- [e2e-api35/shots/ptr-home-dark/f-11.png](e2e-api35/shots/ptr-home-dark/f-11.png)
- [e2e-api35/shots/ptr-home-dark/f-12.png](e2e-api35/shots/ptr-home-dark/f-12.png)
- [e2e-api35/shots/ptr-home-dark/f-13.png](e2e-api35/shots/ptr-home-dark/f-13.png)
- [e2e-api35/shots/ptr-home-dark/f-14.png](e2e-api35/shots/ptr-home-dark/f-14.png)
- [e2e-api35/shots/ptr-home-dark/f-15.png](e2e-api35/shots/ptr-home-dark/f-15.png)
- [e2e-api35/shots/ptr-home-dark/f-16.png](e2e-api35/shots/ptr-home-dark/f-16.png)
- [e2e-api35/shots/ptr-home-dark/f-17.png](e2e-api35/shots/ptr-home-dark/f-17.png)
- [e2e-api35/shots/ptr-home-dark/f-18.png](e2e-api35/shots/ptr-home-dark/f-18.png)
- [e2e-api35/shots/ptr-home-dark/f-19.png](e2e-api35/shots/ptr-home-dark/f-19.png)
- [e2e-api35/shots/ptr-home-dark/f-20.png](e2e-api35/shots/ptr-home-dark/f-20.png)
- [e2e-api35/shots/ptr-home-dark/f-21.png](e2e-api35/shots/ptr-home-dark/f-21.png)
- [e2e-api35/shots/ptr-home-dark/f-22.png](e2e-api35/shots/ptr-home-dark/f-22.png)
- [e2e-api35/shots/ptr-home-dark/f-23.png](e2e-api35/shots/ptr-home-dark/f-23.png)
- [e2e-api35/shots/ptr-home-dark/f-24.png](e2e-api35/shots/ptr-home-dark/f-24.png)
- [e2e-api35/shots/ptr-home-dark/f-25.png](e2e-api35/shots/ptr-home-dark/f-25.png)
- [e2e-api35/shots/ptr-home-dark/f-26.png](e2e-api35/shots/ptr-home-dark/f-26.png)
- [e2e-api35/shots/ptr-home-dark/mid-pull.png](e2e-api35/shots/ptr-home-dark/mid-pull.png)
- [e2e-api35/shots/ptr-home-dark/spinning.png](e2e-api35/shots/ptr-home-dark/spinning.png)

### e2e-api35/shots/ptr-home-light

- [e2e-api35/shots/ptr-home-light/after-release.png](e2e-api35/shots/ptr-home-light/after-release.png)
- [e2e-api35/shots/ptr-home-light/f-01.png](e2e-api35/shots/ptr-home-light/f-01.png)
- [e2e-api35/shots/ptr-home-light/f-02.png](e2e-api35/shots/ptr-home-light/f-02.png)
- [e2e-api35/shots/ptr-home-light/f-03.png](e2e-api35/shots/ptr-home-light/f-03.png)
- [e2e-api35/shots/ptr-home-light/f-04.png](e2e-api35/shots/ptr-home-light/f-04.png)
- [e2e-api35/shots/ptr-home-light/f-05.png](e2e-api35/shots/ptr-home-light/f-05.png)
- [e2e-api35/shots/ptr-home-light/f-06.png](e2e-api35/shots/ptr-home-light/f-06.png)
- [e2e-api35/shots/ptr-home-light/f-07.png](e2e-api35/shots/ptr-home-light/f-07.png)
- [e2e-api35/shots/ptr-home-light/f-08.png](e2e-api35/shots/ptr-home-light/f-08.png)
- [e2e-api35/shots/ptr-home-light/f-09.png](e2e-api35/shots/ptr-home-light/f-09.png)
- [e2e-api35/shots/ptr-home-light/f-10.png](e2e-api35/shots/ptr-home-light/f-10.png)
- [e2e-api35/shots/ptr-home-light/f-11.png](e2e-api35/shots/ptr-home-light/f-11.png)
- [e2e-api35/shots/ptr-home-light/f-12.png](e2e-api35/shots/ptr-home-light/f-12.png)
- [e2e-api35/shots/ptr-home-light/f-13.png](e2e-api35/shots/ptr-home-light/f-13.png)
- [e2e-api35/shots/ptr-home-light/f-14.png](e2e-api35/shots/ptr-home-light/f-14.png)
- [e2e-api35/shots/ptr-home-light/f-15.png](e2e-api35/shots/ptr-home-light/f-15.png)
- [e2e-api35/shots/ptr-home-light/f-16.png](e2e-api35/shots/ptr-home-light/f-16.png)
- [e2e-api35/shots/ptr-home-light/f-17.png](e2e-api35/shots/ptr-home-light/f-17.png)
- [e2e-api35/shots/ptr-home-light/f-18.png](e2e-api35/shots/ptr-home-light/f-18.png)
- [e2e-api35/shots/ptr-home-light/f-19.png](e2e-api35/shots/ptr-home-light/f-19.png)
- [e2e-api35/shots/ptr-home-light/f-20.png](e2e-api35/shots/ptr-home-light/f-20.png)
- [e2e-api35/shots/ptr-home-light/f-21.png](e2e-api35/shots/ptr-home-light/f-21.png)
- [e2e-api35/shots/ptr-home-light/f-22.png](e2e-api35/shots/ptr-home-light/f-22.png)
- [e2e-api35/shots/ptr-home-light/f-23.png](e2e-api35/shots/ptr-home-light/f-23.png)
- [e2e-api35/shots/ptr-home-light/f-24.png](e2e-api35/shots/ptr-home-light/f-24.png)
- [e2e-api35/shots/ptr-home-light/f-25.png](e2e-api35/shots/ptr-home-light/f-25.png)
- [e2e-api35/shots/ptr-home-light/mid-pull.png](e2e-api35/shots/ptr-home-light/mid-pull.png)
- [e2e-api35/shots/ptr-home-light/spinning.png](e2e-api35/shots/ptr-home-light/spinning.png)

### swipe35-e2e-6278bd9/shots/swipe

- [swipe35-e2e-6278bd9/shots/swipe/00-home.png](swipe35-e2e-6278bd9/shots/swipe/00-home.png)
- [swipe35-e2e-6278bd9/shots/swipe/catalog-1.png](swipe35-e2e-6278bd9/shots/swipe/catalog-1.png)
- [swipe35-e2e-6278bd9/shots/swipe/catalog-2.png](swipe35-e2e-6278bd9/shots/swipe/catalog-2.png)
- [swipe35-e2e-6278bd9/shots/swipe/more-1.png](swipe35-e2e-6278bd9/shots/swipe/more-1.png)
- [swipe35-e2e-6278bd9/shots/swipe/more-2.png](swipe35-e2e-6278bd9/shots/swipe/more-2.png)

### swipe35-head-gpu-guest/shots/swipe

- [swipe35-head-gpu-guest/shots/swipe/00-home.png](swipe35-head-gpu-guest/shots/swipe/00-home.png)
- [swipe35-head-gpu-guest/shots/swipe/catalog-1.png](swipe35-head-gpu-guest/shots/swipe/catalog-1.png)
- [swipe35-head-gpu-guest/shots/swipe/catalog-2.png](swipe35-head-gpu-guest/shots/swipe/catalog-2.png)
- [swipe35-head-gpu-guest/shots/swipe/more-1.png](swipe35-head-gpu-guest/shots/swipe/more-1.png)
- [swipe35-head-gpu-guest/shots/swipe/more-2.png](swipe35-head-gpu-guest/shots/swipe/more-2.png)

### swipe35-head/shots/swipe

- [swipe35-head/shots/swipe/00-home.png](swipe35-head/shots/swipe/00-home.png)
- [swipe35-head/shots/swipe/catalog-1.png](swipe35-head/shots/swipe/catalog-1.png)
- [swipe35-head/shots/swipe/catalog-2.png](swipe35-head/shots/swipe/catalog-2.png)
- [swipe35-head/shots/swipe/home-1.png](swipe35-head/shots/swipe/home-1.png)
- [swipe35-head/shots/swipe/home-2.png](swipe35-head/shots/swipe/home-2.png)
- [swipe35-head/shots/swipe/home-6.png](swipe35-head/shots/swipe/home-6.png)
- [swipe35-head/shots/swipe/more-1.png](swipe35-head/shots/swipe/more-1.png)
- [swipe35-head/shots/swipe/more-2.png](swipe35-head/shots/swipe/more-2.png)

### swipe35-v1.2.23/shots/swipe

- [swipe35-v1.2.23/shots/swipe/00-home.png](swipe35-v1.2.23/shots/swipe/00-home.png)
- [swipe35-v1.2.23/shots/swipe/catalog-1.png](swipe35-v1.2.23/shots/swipe/catalog-1.png)
- [swipe35-v1.2.23/shots/swipe/catalog-2.png](swipe35-v1.2.23/shots/swipe/catalog-2.png)
- [swipe35-v1.2.23/shots/swipe/more-1.png](swipe35-v1.2.23/shots/swipe/more-1.png)
- [swipe35-v1.2.23/shots/swipe/more-2.png](swipe35-v1.2.23/shots/swipe/more-2.png)

### swipe35-v1.2.24/shots/swipe

- [swipe35-v1.2.24/shots/swipe/00-home.png](swipe35-v1.2.24/shots/swipe/00-home.png)
- [swipe35-v1.2.24/shots/swipe/catalog-1.png](swipe35-v1.2.24/shots/swipe/catalog-1.png)
- [swipe35-v1.2.24/shots/swipe/catalog-2.png](swipe35-v1.2.24/shots/swipe/catalog-2.png)
- [swipe35-v1.2.24/shots/swipe/home-1.png](swipe35-v1.2.24/shots/swipe/home-1.png)
- [swipe35-v1.2.24/shots/swipe/home-2.png](swipe35-v1.2.24/shots/swipe/home-2.png)
- [swipe35-v1.2.24/shots/swipe/more-1.png](swipe35-v1.2.24/shots/swipe/more-1.png)
- [swipe35-v1.2.24/shots/swipe/more-2.png](swipe35-v1.2.24/shots/swipe/more-2.png)

## Other files

- [e2e-api29/apk-contents.txt](e2e-api29/apk-contents.txt)
- [e2e-api29/guest-dmesg.txt](e2e-api29/guest-dmesg.txt)
- [e2e-api29/host-after-run.txt](e2e-api29/host-after-run.txt)
- [e2e-api29/host-mon.txt](e2e-api29/host-mon.txt)
- [e2e-api29/intro.mp4](e2e-api29/intro.mp4)
- [e2e-api29/logcat-all.txt](e2e-api29/logcat-all.txt)
- [e2e-api29/logcat-dump.txt](e2e-api29/logcat-dump.txt)
- [e2e-api29/logcat-findings.txt](e2e-api29/logcat-findings.txt)
- [e2e-api29/logcat-live.txt](e2e-api29/logcat-live.txt)
- [e2e-api29/ptr-home-dark.mp4](e2e-api29/ptr-home-dark.mp4)
- [e2e-api29/ptr-home-light.mp4](e2e-api29/ptr-home-light.mp4)
- [e2e-api29/screenrecord-ptr-home-dark.txt](e2e-api29/screenrecord-ptr-home-dark.txt)
- [e2e-api29/screenrecord-ptr-home-light.txt](e2e-api29/screenrecord-ptr-home-light.txt)
- [e2e-api29/screenrecord.txt](e2e-api29/screenrecord.txt)
- [e2e-api29/summary.txt](e2e-api29/summary.txt)
- [e2e-api29/window/catalog-dark-grep.txt](e2e-api29/window/catalog-dark-grep.txt)
- [e2e-api29/window/catalog-dark.txt](e2e-api29/window/catalog-dark.txt)
- [e2e-api29/window/catalog-light-grep.txt](e2e-api29/window/catalog-light-grep.txt)
- [e2e-api29/window/catalog-light.txt](e2e-api29/window/catalog-light.txt)
- [e2e-api29/window/home-3btn-dark-grep.txt](e2e-api29/window/home-3btn-dark-grep.txt)
- [e2e-api29/window/home-3btn-dark.txt](e2e-api29/window/home-3btn-dark.txt)
- [e2e-api29/window/home-3btn-light-grep.txt](e2e-api29/window/home-3btn-light-grep.txt)
- [e2e-api29/window/home-3btn-light.txt](e2e-api29/window/home-3btn-light.txt)
- [e2e-api29/window/home-appdark-syslight-grep.txt](e2e-api29/window/home-appdark-syslight-grep.txt)
- [e2e-api29/window/home-appdark-syslight.txt](e2e-api29/window/home-appdark-syslight.txt)
- [e2e-api29/window/home-applight-sysdark-grep.txt](e2e-api29/window/home-applight-sysdark-grep.txt)
- [e2e-api29/window/home-applight-sysdark.txt](e2e-api29/window/home-applight-sysdark.txt)
- [e2e-api29/window/home-dark-grep.txt](e2e-api29/window/home-dark-grep.txt)
- [e2e-api29/window/home-dark.txt](e2e-api29/window/home-dark.txt)
- [e2e-api29/window/home-gesture-light-grep.txt](e2e-api29/window/home-gesture-light-grep.txt)
- [e2e-api29/window/home-gesture-light.txt](e2e-api29/window/home-gesture-light.txt)
- [e2e-api29/window/home-light-grep.txt](e2e-api29/window/home-light-grep.txt)
- [e2e-api29/window/home-light.txt](e2e-api29/window/home-light.txt)
- [e2e-api29/window/land270-catalog-grep.txt](e2e-api29/window/land270-catalog-grep.txt)
- [e2e-api29/window/land270-catalog.txt](e2e-api29/window/land270-catalog.txt)
- [e2e-api29/window/land90-home-grep.txt](e2e-api29/window/land90-home-grep.txt)
- [e2e-api29/window/land90-home.txt](e2e-api29/window/land90-home.txt)
- [e2e-api29/window/onboarding-grep.txt](e2e-api29/window/onboarding-grep.txt)
- [e2e-api29/window/onboarding.txt](e2e-api29/window/onboarding.txt)
- [e2e-api35/apk-contents.txt](e2e-api35/apk-contents.txt)
- [e2e-api35/guest-dmesg.txt](e2e-api35/guest-dmesg.txt)
- [e2e-api35/host-after-run.txt](e2e-api35/host-after-run.txt)
- [e2e-api35/host-mon.txt](e2e-api35/host-mon.txt)
- [e2e-api35/intro.mp4](e2e-api35/intro.mp4)
- [e2e-api35/logcat-all.txt](e2e-api35/logcat-all.txt)
- [e2e-api35/logcat-dump.txt](e2e-api35/logcat-dump.txt)
- [e2e-api35/logcat-findings.txt](e2e-api35/logcat-findings.txt)
- [e2e-api35/logcat-live.txt](e2e-api35/logcat-live.txt)
- [e2e-api35/ptr-home-dark.mp4](e2e-api35/ptr-home-dark.mp4)
- [e2e-api35/ptr-home-light.mp4](e2e-api35/ptr-home-light.mp4)
- [e2e-api35/screenrecord-ptr-home-dark.txt](e2e-api35/screenrecord-ptr-home-dark.txt)
- [e2e-api35/screenrecord-ptr-home-light.txt](e2e-api35/screenrecord-ptr-home-light.txt)
- [e2e-api35/screenrecord.txt](e2e-api35/screenrecord.txt)
- [e2e-api35/summary.txt](e2e-api35/summary.txt)
- [e2e-api35/window/catalog-dark-grep.txt](e2e-api35/window/catalog-dark-grep.txt)
- [e2e-api35/window/catalog-dark.txt](e2e-api35/window/catalog-dark.txt)
- [e2e-api35/window/catalog-light-grep.txt](e2e-api35/window/catalog-light-grep.txt)
- [e2e-api35/window/catalog-light.txt](e2e-api35/window/catalog-light.txt)
- [e2e-api35/window/home-3btn-dark-grep.txt](e2e-api35/window/home-3btn-dark-grep.txt)
- [e2e-api35/window/home-3btn-dark.txt](e2e-api35/window/home-3btn-dark.txt)
- [e2e-api35/window/home-3btn-light-grep.txt](e2e-api35/window/home-3btn-light-grep.txt)
- [e2e-api35/window/home-3btn-light.txt](e2e-api35/window/home-3btn-light.txt)
- [e2e-api35/window/home-appdark-syslight-grep.txt](e2e-api35/window/home-appdark-syslight-grep.txt)
- [e2e-api35/window/home-appdark-syslight.txt](e2e-api35/window/home-appdark-syslight.txt)
- [e2e-api35/window/home-applight-sysdark-grep.txt](e2e-api35/window/home-applight-sysdark-grep.txt)
- [e2e-api35/window/home-applight-sysdark.txt](e2e-api35/window/home-applight-sysdark.txt)
- [e2e-api35/window/home-dark-grep.txt](e2e-api35/window/home-dark-grep.txt)
- [e2e-api35/window/home-dark.txt](e2e-api35/window/home-dark.txt)
- [e2e-api35/window/home-gesture-light-grep.txt](e2e-api35/window/home-gesture-light-grep.txt)
- [e2e-api35/window/home-gesture-light.txt](e2e-api35/window/home-gesture-light.txt)
- [e2e-api35/window/home-light-grep.txt](e2e-api35/window/home-light-grep.txt)
- [e2e-api35/window/home-light.txt](e2e-api35/window/home-light.txt)
- [e2e-api35/window/land270-catalog-grep.txt](e2e-api35/window/land270-catalog-grep.txt)
- [e2e-api35/window/land270-catalog.txt](e2e-api35/window/land270-catalog.txt)
- [e2e-api35/window/land90-home-grep.txt](e2e-api35/window/land90-home-grep.txt)
- [e2e-api35/window/land90-home.txt](e2e-api35/window/land90-home.txt)
- [e2e-api35/window/onboarding-grep.txt](e2e-api35/window/onboarding-grep.txt)
- [e2e-api35/window/onboarding.txt](e2e-api35/window/onboarding.txt)
- [swipe35-e2e-6278bd9/apk-contents.txt](swipe35-e2e-6278bd9/apk-contents.txt)
- [swipe35-e2e-6278bd9/guest-dmesg.txt](swipe35-e2e-6278bd9/guest-dmesg.txt)
- [swipe35-e2e-6278bd9/host-after-run.txt](swipe35-e2e-6278bd9/host-after-run.txt)
- [swipe35-e2e-6278bd9/host-evidence.txt](swipe35-e2e-6278bd9/host-evidence.txt)
- [swipe35-e2e-6278bd9/host-mon.txt](swipe35-e2e-6278bd9/host-mon.txt)
- [swipe35-e2e-6278bd9/logcat-all.txt](swipe35-e2e-6278bd9/logcat-all.txt)
- [swipe35-e2e-6278bd9/logcat-dump.txt](swipe35-e2e-6278bd9/logcat-dump.txt)
- [swipe35-e2e-6278bd9/logcat-findings.txt](swipe35-e2e-6278bd9/logcat-findings.txt)
- [swipe35-e2e-6278bd9/logcat-live.txt](swipe35-e2e-6278bd9/logcat-live.txt)
- [swipe35-e2e-6278bd9/summary.txt](swipe35-e2e-6278bd9/summary.txt)
- [swipe35-head-gpu-guest/apk-contents.txt](swipe35-head-gpu-guest/apk-contents.txt)
- [swipe35-head-gpu-guest/guest-dmesg.txt](swipe35-head-gpu-guest/guest-dmesg.txt)
- [swipe35-head-gpu-guest/host-after-run.txt](swipe35-head-gpu-guest/host-after-run.txt)
- [swipe35-head-gpu-guest/host-evidence.txt](swipe35-head-gpu-guest/host-evidence.txt)
- [swipe35-head-gpu-guest/host-mon.txt](swipe35-head-gpu-guest/host-mon.txt)
- [swipe35-head-gpu-guest/logcat-all.txt](swipe35-head-gpu-guest/logcat-all.txt)
- [swipe35-head-gpu-guest/logcat-dump.txt](swipe35-head-gpu-guest/logcat-dump.txt)
- [swipe35-head-gpu-guest/logcat-findings.txt](swipe35-head-gpu-guest/logcat-findings.txt)
- [swipe35-head-gpu-guest/logcat-live.txt](swipe35-head-gpu-guest/logcat-live.txt)
- [swipe35-head-gpu-guest/summary.txt](swipe35-head-gpu-guest/summary.txt)
- [swipe35-head/apk-contents.txt](swipe35-head/apk-contents.txt)
- [swipe35-head/guest-dmesg.txt](swipe35-head/guest-dmesg.txt)
- [swipe35-head/host-after-run.txt](swipe35-head/host-after-run.txt)
- [swipe35-head/host-evidence.txt](swipe35-head/host-evidence.txt)
- [swipe35-head/host-mon.txt](swipe35-head/host-mon.txt)
- [swipe35-head/logcat-all.txt](swipe35-head/logcat-all.txt)
- [swipe35-head/logcat-dump.txt](swipe35-head/logcat-dump.txt)
- [swipe35-head/logcat-findings.txt](swipe35-head/logcat-findings.txt)
- [swipe35-head/logcat-live.txt](swipe35-head/logcat-live.txt)
- [swipe35-head/summary.txt](swipe35-head/summary.txt)
- [swipe35-v1.2.23/apk-contents.txt](swipe35-v1.2.23/apk-contents.txt)
- [swipe35-v1.2.23/guest-dmesg.txt](swipe35-v1.2.23/guest-dmesg.txt)
- [swipe35-v1.2.23/host-after-run.txt](swipe35-v1.2.23/host-after-run.txt)
- [swipe35-v1.2.23/host-evidence.txt](swipe35-v1.2.23/host-evidence.txt)
- [swipe35-v1.2.23/host-mon.txt](swipe35-v1.2.23/host-mon.txt)
- [swipe35-v1.2.23/logcat-all.txt](swipe35-v1.2.23/logcat-all.txt)
- [swipe35-v1.2.23/logcat-dump.txt](swipe35-v1.2.23/logcat-dump.txt)
- [swipe35-v1.2.23/logcat-findings.txt](swipe35-v1.2.23/logcat-findings.txt)
- [swipe35-v1.2.23/logcat-live.txt](swipe35-v1.2.23/logcat-live.txt)
- [swipe35-v1.2.23/summary.txt](swipe35-v1.2.23/summary.txt)
- [swipe35-v1.2.24/apk-contents.txt](swipe35-v1.2.24/apk-contents.txt)
- [swipe35-v1.2.24/guest-dmesg.txt](swipe35-v1.2.24/guest-dmesg.txt)
- [swipe35-v1.2.24/host-after-run.txt](swipe35-v1.2.24/host-after-run.txt)
- [swipe35-v1.2.24/host-evidence.txt](swipe35-v1.2.24/host-evidence.txt)
- [swipe35-v1.2.24/host-mon.txt](swipe35-v1.2.24/host-mon.txt)
- [swipe35-v1.2.24/logcat-all.txt](swipe35-v1.2.24/logcat-all.txt)
- [swipe35-v1.2.24/logcat-dump.txt](swipe35-v1.2.24/logcat-dump.txt)
- [swipe35-v1.2.24/logcat-findings.txt](swipe35-v1.2.24/logcat-findings.txt)
- [swipe35-v1.2.24/logcat-live.txt](swipe35-v1.2.24/logcat-live.txt)
- [swipe35-v1.2.24/summary.txt](swipe35-v1.2.24/summary.txt)
