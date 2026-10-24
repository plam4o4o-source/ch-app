# Edge-to-edge emulator results

Second round (fixes 09278c0, pull-to-refresh, API 35 swipe bisection): [run2/index.md](run2/index.md)

Source: `debug/e2e-check` @ 98b66e9, run 40103313078 (attempt 1), 2026-10-24T15:11:52Z.
Emulators: API 29 and API 35, google_apis x86_64, pixel_6 profile, swiftshader (software rendering).

## api29 summary

```
APK size: 4810045 bytes
APK entries:   8725896                     1031 files
screen 1080x2400
## 0. Device
android 10 sdk 29; Physical size: 1080x2400; Physical density: 420
NAV gestural: navigation_mode=2; overlays: [ ] com.android.internal.systemui.navbar.gestural_wide_back [ ] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.gestural_extra_wide_back [x] com.android.internal.systemui.navbar.gestural [ ] com.android.internal.systemui.navbar.gestural_narrow_back 
cutout before: Cutout=WmDisplayCutout{DisplayCutout{insets=Rect(0, 0 - 0, 0) boundingRect={Bounds=[Rect(0, 0 
cutout overlays: [ ] com.android.internal.display.cutout.emulation.corner [ ] com.android.internal.display.cutout.emulation.double [ ] com.android.internal.display.cutout.emulation.tall [ ] com.android.internal.display.cutout.emulation.emu01 
CUTOUT enabled: tall; overlays: [ ] com.android.internal.display.cutout.emulation.corner [ ] com.android.internal.display.cutout.emulation.double [x] com.android.internal.display.cutout.emulation.tall [ ] com.android.internal.display.cutout.emulation.emu01 
cutout after: Cutout=WmDisplayCutout{DisplayCutout{insets=Rect(0, 0 - 0, 0) boundingRect={Bounds=[Rect(0, 0 
screen 1080x2400
## 1. Fresh install: splash, intro, first onboarding page (animations ON)
intro video: 483138 bytes, frames: 16
WIN onboarding: Window{30b5f49 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x700  mFrame=[0,0][1080,2400]
## 2. Skip onboarding; Home light
WIN home-light: Window{30b5f49 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][1080,2400]
WIN catalog-light: Window{30b5f49 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][1080,2400]
## 3. System dark (app theme = System)
NIGHT yes via Settings: after 1 polls: Night mode: yes
NIGHT yes: Night mode: yes ; ui_night_mode=2
WIN home-dark: Window{30b5f49 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x700  mFrame=[0,0][1080,2400]
WIN catalog-dark: Window{30b5f49 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x700  mFrame=[0,0][1080,2400]
## 4. App Light in a dark system
THEME -> =Light|=Светла (settings now shows: Light)
WIN home-applight-sysdark: Window{30b5f49 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][1080,2400]
## 4b. App Dark in a light system
NIGHT no via Settings: after 1 polls: Night mode: no
NIGHT no: Night mode: no ; ui_night_mode=1
THEME -> =Dark|=Тъмна (settings now shows: Dark)
WIN home-appdark-syslight: Window{30b5f49 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x700  mFrame=[0,0][1080,2400]
THEME -> =System default|=Както системата (settings now shows: System default)
## 5. 3-button navigation
NAV threebutton: navigation_mode=0; overlays: [ ] com.android.internal.systemui.navbar.gestural_wide_back [x] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.gestural_extra_wide_back [ ] com.android.internal.systemui.navbar.gestural [ ] com.android.internal.systemui.navbar.gestural_narrow_back 
WIN home-3btn-light: Window{30b5f49 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][1080,2400]
NIGHT yes via Settings: after 1 polls: Night mode: yes
NIGHT yes: Night mode: yes ; ui_night_mode=2
WIN home-3btn-dark: Window{30b5f49 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x700  mFrame=[0,0][1080,2400]
NIGHT no via Settings: after 1 polls: Night mode: no
NIGHT no: Night mode: no ; ui_night_mode=1
## 6. Back to gesture navigation
NAV gestural: navigation_mode=2; overlays: [ ] com.android.internal.systemui.navbar.gestural_wide_back [ ] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.gestural_extra_wide_back [x] com.android.internal.systemui.navbar.gestural [ ] com.android.internal.systemui.navbar.gestural_narrow_back 
WIN home-gesture-light: Window{30b5f49 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][1080,2400]
## 7. Landscape with 3-button navigation (cutout)
NAV threebutton: navigation_mode=0; overlays: [ ] com.android.internal.systemui.navbar.gestural_wide_back [x] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.gestural_extra_wide_back [ ] com.android.internal.systemui.navbar.gestural [ ] com.android.internal.systemui.navbar.gestural_narrow_back 
ROTATION 1: mCurrentRotation=ROTATION_90 cur=2400x1080
WIN land90-home: Window{30b5f49 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight land  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][2400,1080]
ROTATION 3: mCurrentRotation=ROTATION_270 cur=2400x1080
WIN land270-catalog: Window{30b5f49 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight land  cutoutMode=always  vsysui=[LAYOUT_STABLE LAYOUT_HIDE_NAVIGATION LAYOUT_FULLSCREEN LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[FORCE_DRAW_STATUS_BAR_BACKGROUND]  mSystemUiVisibility=0x2710  mFrame=[0,0][2400,1080]
NIGHT yes via Settings: after 1 polls: Night mode: yes
NIGHT yes: Night mode: yes ; ui_night_mode=2
ROTATION 1: mCurrentRotation=ROTATION_90 cur=2400x1080
NIGHT no via Settings: after 1 polls: Night mode: no
NIGHT no: Night mode: no ; ui_night_mode=1
ROTATION 0: mCurrentRotation=ROTATION_0 cur=1080x2400
## 8. Scrolled content under the bars (3-button, then gestures)
NAV gestural: navigation_mode=2; overlays: [ ] com.android.internal.systemui.navbar.gestural_wide_back [ ] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.gestural_extra_wide_back [x] com.android.internal.systemui.navbar.gestural [ ] com.android.internal.systemui.navbar.gestural_narrow_back 
NIGHT yes via Settings: after 1 polls: Night mode: yes
NIGHT yes: Night mode: yes ; ui_night_mode=2
NIGHT no via Settings: after 1 polls: Night mode: no
NIGHT no: Night mode: no ; ui_night_mode=1
## COMPLETE
## logcat findings
app pids seen: ['6676']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=67 total=100
== Davey: 3
   davey events=3 max=1255ms median=1186ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## api35 summary

```
(CARRIED OVER from the previous publish (Edge-to-edge emulator results for d8f241f (run 40100568335)): api35 was not run or failed in this run)
APK size: 4810045 bytes
APK entries:   8725896                     1031 files
screen 1080x2400
## 0. Device
android 15 sdk 35; Physical size: 1080x2400; Physical density: 420
NAV gestural: navigation_mode=2; overlays: [ ] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.transparent [x] com.android.internal.systemui.navbar.gestural 
cutout before: Cutout=DisplayCutout{insets=Rect(0, 128 - 0, 0) waterfall=Insets{left=0, top=0, 
cutout overlays: [ ] com.android.internal.display.cutout.emulation.corner [ ] com.android.internal.display.cutout.emulation.double [ ] com.android.internal.display.cutout.emulation.hole [ ] com.android.internal.display.cutout.emulation.tall [ ] com.android.internal.display.cutout.emulation.emu01 [ ] com.android.internal.display.cutout.emulation.waterfall 
CUTOUT: native cutout present, no emulation overlay
screen 1080x2400
## 1. Fresh install: splash, intro, first onboarding page (animations ON)
intro video: 500016 bytes, frames: 18
WIN onboarding: Window{3f78fd2 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
## 2. Skip onboarding; Home light
WIN home-light: Window{3f78fd2 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
WIN catalog-light: Window{3f78fd2 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
## 3. System dark (app theme = System)
NIGHT yes: Night mode: yes ; ui_night_mode=2
WIN home-dark: Window{3f78fd2 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
WIN catalog-dark: Window{3f78fd2 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
## 4. App Light in a dark system
THEME -> =Light|=Светла (settings now shows: Light)
WIN home-applight-sysdark: Window{3f78fd2 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
## 4b. App Dark in a light system
NIGHT no: Night mode: no ; ui_night_mode=1
THEME -> =Dark|=Тъмна (settings now shows: Dark)
WIN home-appdark-syslight: Window{3f78fd2 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
THEME -> =System default|=Както системата (settings now shows: System default)
## 5. 3-button navigation
NAV threebutton: navigation_mode=0; overlays: [x] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.transparent [ ] com.android.internal.systemui.navbar.gestural 
WIN home-3btn-light: Window{b3dd1c1 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
NIGHT yes: Night mode: yes ; ui_night_mode=2
WIN home-3btn-dark: Window{b3dd1c1 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=night port  cutoutMode=always  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
NIGHT no: Night mode: no ; ui_night_mode=1
## 6. Back to gesture navigation
NAV gestural: navigation_mode=2; overlays: [ ] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.transparent [x] com.android.internal.systemui.navbar.gestural 
WIN home-gesture-light: Window{d079b5 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight port  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
## 7. Landscape with 3-button navigation (cutout)
NAV threebutton: navigation_mode=0; overlays: [x] com.android.internal.systemui.navbar.threebutton [ ] com.android.internal.systemui.navbar.twobutton [ ] com.android.internal.systemui.navbar.transparent [ ] com.android.internal.systemui.navbar.gestural 
ROTATION 1: mRotation=ROTATION_90} cur=2400x1080
WIN land90-home: Window{81b8943 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight land  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
ROTATION 3: mRotation=ROTATION_270} cur=2400x1080
WIN land270-catalog: Window{81b8943 u0 org.chyavorec.app/org.chyavorec.app.MainActivity}  config=notnight land  cutoutMode=always  apr=[LIGHT_STATUS_BARS LIGHT_NAVIGATION_BARS]  vsysui=[LIGHT_STATUS_BAR LIGHT_NAVIGATION_BAR]  bhv=[DEFAULT]  fl=[LAYOUT_IN_SCREEN LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS]  pfl=[NO_MOVE_ANIMATION EDGE_TO_EDGE_ENFORCED FORCE_DRAW_STATUS_BAR_BACKGROUND FIT_INSETS_CONTROLLED]
NIGHT yes: Night mode: yes ; ui_night_mode=2
ROTATION 1: mRotation=ROTATION_90} cur=2400x1080
NIGHT no: Night mode: no ; ui_night_mode=1
ROTATION 0: mRotation=ROTATION_0} cur=1080x2400
## 8. Scrolled content under the bars (3-button, then gestures)
WARN: no catalogue results
NAV gestural: navigation_mode=; overlays: 
NIGHT yes: ; ui_night_mode=
NIGHT no: ; ui_night_mode=
!!! EMULATOR GONE (at: end)
## logcat findings
app pids seen: ['4151']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 3
   skipped-frames events=3 max=38 total=98
== Davey: 3
   davey events=3 max=1084ms median=946ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Screenshots

### api29/shots

- [api29/shots/01-intro-01.png](api29/shots/01-intro-01.png)
- [api29/shots/01-intro-02.png](api29/shots/01-intro-02.png)
- [api29/shots/01-intro-03.png](api29/shots/01-intro-03.png)
- [api29/shots/01-intro-04.png](api29/shots/01-intro-04.png)
- [api29/shots/01-intro-05.png](api29/shots/01-intro-05.png)
- [api29/shots/01-intro-06.png](api29/shots/01-intro-06.png)
- [api29/shots/01-intro-07.png](api29/shots/01-intro-07.png)
- [api29/shots/01-intro-08.png](api29/shots/01-intro-08.png)
- [api29/shots/01-intro-09.png](api29/shots/01-intro-09.png)
- [api29/shots/01-intro-10.png](api29/shots/01-intro-10.png)
- [api29/shots/01-intro-11.png](api29/shots/01-intro-11.png)
- [api29/shots/01-intro-12.png](api29/shots/01-intro-12.png)
- [api29/shots/01-intro-13.png](api29/shots/01-intro-13.png)
- [api29/shots/01-intro-14.png](api29/shots/01-intro-14.png)
- [api29/shots/01-intro-15.png](api29/shots/01-intro-15.png)
- [api29/shots/01-intro-16.png](api29/shots/01-intro-16.png)
- [api29/shots/02-onboarding-1.png](api29/shots/02-onboarding-1.png)
- [api29/shots/03-home-light.png](api29/shots/03-home-light.png)
- [api29/shots/03c-catalog-light.png](api29/shots/03c-catalog-light.png)
- [api29/shots/04-home-dark.png](api29/shots/04-home-dark.png)
- [api29/shots/05-catalog-dark.png](api29/shots/05-catalog-dark.png)
- [api29/shots/06-home-applight-sysdark.png](api29/shots/06-home-applight-sysdark.png)
- [api29/shots/06a-settings-applight-sysdark.png](api29/shots/06a-settings-applight-sysdark.png)
- [api29/shots/06b-catalog-applight-sysdark.png](api29/shots/06b-catalog-applight-sysdark.png)
- [api29/shots/07-home-appdark-syslight.png](api29/shots/07-home-appdark-syslight.png)
- [api29/shots/07a-settings-appdark-syslight.png](api29/shots/07a-settings-appdark-syslight.png)
- [api29/shots/07b-catalog-appdark-syslight.png](api29/shots/07b-catalog-appdark-syslight.png)
- [api29/shots/08-home-3btn-light.png](api29/shots/08-home-3btn-light.png)
- [api29/shots/08c-catalog-3btn-light.png](api29/shots/08c-catalog-3btn-light.png)
- [api29/shots/09-home-3btn-dark.png](api29/shots/09-home-3btn-dark.png)
- [api29/shots/09c-catalog-3btn-dark.png](api29/shots/09c-catalog-3btn-dark.png)
- [api29/shots/10-home-gesture-light.png](api29/shots/10-home-gesture-light.png)
- [api29/shots/11-land90-home.png](api29/shots/11-land90-home.png)
- [api29/shots/12-land90-catalog.png](api29/shots/12-land90-catalog.png)
- [api29/shots/13-land270-catalog.png](api29/shots/13-land270-catalog.png)
- [api29/shots/14-land270-home.png](api29/shots/14-land270-home.png)
- [api29/shots/15-land270-home-dark.png](api29/shots/15-land270-home-dark.png)
- [api29/shots/16-land90-home-dark.png](api29/shots/16-land90-home-dark.png)
- [api29/shots/17-home-3btn-light-scrolled.png](api29/shots/17-home-3btn-light-scrolled.png)
- [api29/shots/18-catalog-3btn-light-scrolled.png](api29/shots/18-catalog-3btn-light-scrolled.png)
- [api29/shots/19-home-gesture-light-scrolled.png](api29/shots/19-home-gesture-light-scrolled.png)
- [api29/shots/20-home-gesture-dark-scrolled.png](api29/shots/20-home-gesture-dark-scrolled.png)

### api29/shots/debug

- [api29/shots/debug/night-no-1-a-before.png](api29/shots/debug/night-no-1-a-before.png)
- [api29/shots/debug/night-no-1-b-after.png](api29/shots/debug/night-no-1-b-after.png)
- [api29/shots/debug/night-no-3-a-before.png](api29/shots/debug/night-no-3-a-before.png)
- [api29/shots/debug/night-no-3-b-after.png](api29/shots/debug/night-no-3-b-after.png)
- [api29/shots/debug/night-no-5-a-before.png](api29/shots/debug/night-no-5-a-before.png)
- [api29/shots/debug/night-no-5-b-after.png](api29/shots/debug/night-no-5-b-after.png)
- [api29/shots/debug/night-no-7-a-before.png](api29/shots/debug/night-no-7-a-before.png)
- [api29/shots/debug/night-no-7-b-after.png](api29/shots/debug/night-no-7-b-after.png)
- [api29/shots/debug/night-yes-0-a-before.png](api29/shots/debug/night-yes-0-a-before.png)
- [api29/shots/debug/night-yes-0-b-after.png](api29/shots/debug/night-yes-0-b-after.png)
- [api29/shots/debug/night-yes-2-a-before.png](api29/shots/debug/night-yes-2-a-before.png)
- [api29/shots/debug/night-yes-2-b-after.png](api29/shots/debug/night-yes-2-b-after.png)
- [api29/shots/debug/night-yes-4-a-before.png](api29/shots/debug/night-yes-4-a-before.png)
- [api29/shots/debug/night-yes-4-b-after.png](api29/shots/debug/night-yes-4-b-after.png)
- [api29/shots/debug/night-yes-6-a-before.png](api29/shots/debug/night-yes-6-a-before.png)
- [api29/shots/debug/night-yes-6-b-after.png](api29/shots/debug/night-yes-6-b-after.png)

### api35/shots

- [api35/shots/01-intro-01.png](api35/shots/01-intro-01.png)
- [api35/shots/01-intro-02.png](api35/shots/01-intro-02.png)
- [api35/shots/01-intro-03.png](api35/shots/01-intro-03.png)
- [api35/shots/01-intro-04.png](api35/shots/01-intro-04.png)
- [api35/shots/01-intro-05.png](api35/shots/01-intro-05.png)
- [api35/shots/01-intro-06.png](api35/shots/01-intro-06.png)
- [api35/shots/01-intro-07.png](api35/shots/01-intro-07.png)
- [api35/shots/01-intro-08.png](api35/shots/01-intro-08.png)
- [api35/shots/01-intro-09.png](api35/shots/01-intro-09.png)
- [api35/shots/01-intro-10.png](api35/shots/01-intro-10.png)
- [api35/shots/01-intro-11.png](api35/shots/01-intro-11.png)
- [api35/shots/01-intro-12.png](api35/shots/01-intro-12.png)
- [api35/shots/01-intro-13.png](api35/shots/01-intro-13.png)
- [api35/shots/01-intro-14.png](api35/shots/01-intro-14.png)
- [api35/shots/01-intro-15.png](api35/shots/01-intro-15.png)
- [api35/shots/01-intro-16.png](api35/shots/01-intro-16.png)
- [api35/shots/01-intro-17.png](api35/shots/01-intro-17.png)
- [api35/shots/01-intro-18.png](api35/shots/01-intro-18.png)
- [api35/shots/02-onboarding-1.png](api35/shots/02-onboarding-1.png)
- [api35/shots/03-home-light.png](api35/shots/03-home-light.png)
- [api35/shots/03c-catalog-light.png](api35/shots/03c-catalog-light.png)
- [api35/shots/04-home-dark.png](api35/shots/04-home-dark.png)
- [api35/shots/05-catalog-dark.png](api35/shots/05-catalog-dark.png)
- [api35/shots/06-home-applight-sysdark.png](api35/shots/06-home-applight-sysdark.png)
- [api35/shots/06a-settings-applight-sysdark.png](api35/shots/06a-settings-applight-sysdark.png)
- [api35/shots/06b-catalog-applight-sysdark.png](api35/shots/06b-catalog-applight-sysdark.png)
- [api35/shots/07-home-appdark-syslight.png](api35/shots/07-home-appdark-syslight.png)
- [api35/shots/07a-settings-appdark-syslight.png](api35/shots/07a-settings-appdark-syslight.png)
- [api35/shots/07b-catalog-appdark-syslight.png](api35/shots/07b-catalog-appdark-syslight.png)
- [api35/shots/08-home-3btn-light.png](api35/shots/08-home-3btn-light.png)
- [api35/shots/08c-catalog-3btn-light.png](api35/shots/08c-catalog-3btn-light.png)
- [api35/shots/09-home-3btn-dark.png](api35/shots/09-home-3btn-dark.png)
- [api35/shots/09c-catalog-3btn-dark.png](api35/shots/09c-catalog-3btn-dark.png)
- [api35/shots/10-home-gesture-light.png](api35/shots/10-home-gesture-light.png)
- [api35/shots/11-land90-home.png](api35/shots/11-land90-home.png)
- [api35/shots/12-land90-catalog.png](api35/shots/12-land90-catalog.png)
- [api35/shots/13-land270-catalog.png](api35/shots/13-land270-catalog.png)
- [api35/shots/14-land270-home.png](api35/shots/14-land270-home.png)
- [api35/shots/15-land270-home-dark.png](api35/shots/15-land270-home-dark.png)
- [api35/shots/16-land90-home-dark.png](api35/shots/16-land90-home-dark.png)

## Other files

- [api29/apk-contents.txt](api29/apk-contents.txt)
- [api29/intro.mp4](api29/intro.mp4)
- [api29/logcat-all.txt](api29/logcat-all.txt)
- [api29/logcat-dump.txt](api29/logcat-dump.txt)
- [api29/logcat-findings.txt](api29/logcat-findings.txt)
- [api29/logcat-live.txt](api29/logcat-live.txt)
- [api29/screenrecord.txt](api29/screenrecord.txt)
- [api29/summary.txt](api29/summary.txt)
- [api29/window/catalog-dark-grep.txt](api29/window/catalog-dark-grep.txt)
- [api29/window/catalog-dark.txt](api29/window/catalog-dark.txt)
- [api29/window/catalog-light-grep.txt](api29/window/catalog-light-grep.txt)
- [api29/window/catalog-light.txt](api29/window/catalog-light.txt)
- [api29/window/home-3btn-dark-grep.txt](api29/window/home-3btn-dark-grep.txt)
- [api29/window/home-3btn-dark.txt](api29/window/home-3btn-dark.txt)
- [api29/window/home-3btn-light-grep.txt](api29/window/home-3btn-light-grep.txt)
- [api29/window/home-3btn-light.txt](api29/window/home-3btn-light.txt)
- [api29/window/home-appdark-syslight-grep.txt](api29/window/home-appdark-syslight-grep.txt)
- [api29/window/home-appdark-syslight.txt](api29/window/home-appdark-syslight.txt)
- [api29/window/home-applight-sysdark-grep.txt](api29/window/home-applight-sysdark-grep.txt)
- [api29/window/home-applight-sysdark.txt](api29/window/home-applight-sysdark.txt)
- [api29/window/home-dark-grep.txt](api29/window/home-dark-grep.txt)
- [api29/window/home-dark.txt](api29/window/home-dark.txt)
- [api29/window/home-gesture-light-grep.txt](api29/window/home-gesture-light-grep.txt)
- [api29/window/home-gesture-light.txt](api29/window/home-gesture-light.txt)
- [api29/window/home-light-grep.txt](api29/window/home-light-grep.txt)
- [api29/window/home-light.txt](api29/window/home-light.txt)
- [api29/window/land270-catalog-grep.txt](api29/window/land270-catalog-grep.txt)
- [api29/window/land270-catalog.txt](api29/window/land270-catalog.txt)
- [api29/window/land90-home-grep.txt](api29/window/land90-home-grep.txt)
- [api29/window/land90-home.txt](api29/window/land90-home.txt)
- [api29/window/onboarding-grep.txt](api29/window/onboarding-grep.txt)
- [api29/window/onboarding.txt](api29/window/onboarding.txt)
- [api35/apk-contents.txt](api35/apk-contents.txt)
- [api35/intro.mp4](api35/intro.mp4)
- [api35/logcat-all.txt](api35/logcat-all.txt)
- [api35/logcat-dump.txt](api35/logcat-dump.txt)
- [api35/logcat-findings.txt](api35/logcat-findings.txt)
- [api35/logcat-live.txt](api35/logcat-live.txt)
- [api35/screenrecord.txt](api35/screenrecord.txt)
- [api35/summary.txt](api35/summary.txt)
- [api35/window/catalog-dark-grep.txt](api35/window/catalog-dark-grep.txt)
- [api35/window/catalog-dark.txt](api35/window/catalog-dark.txt)
- [api35/window/catalog-light-grep.txt](api35/window/catalog-light-grep.txt)
- [api35/window/catalog-light.txt](api35/window/catalog-light.txt)
- [api35/window/home-3btn-dark-grep.txt](api35/window/home-3btn-dark-grep.txt)
- [api35/window/home-3btn-dark.txt](api35/window/home-3btn-dark.txt)
- [api35/window/home-3btn-light-grep.txt](api35/window/home-3btn-light-grep.txt)
- [api35/window/home-3btn-light.txt](api35/window/home-3btn-light.txt)
- [api35/window/home-appdark-syslight-grep.txt](api35/window/home-appdark-syslight-grep.txt)
- [api35/window/home-appdark-syslight.txt](api35/window/home-appdark-syslight.txt)
- [api35/window/home-applight-sysdark-grep.txt](api35/window/home-applight-sysdark-grep.txt)
- [api35/window/home-applight-sysdark.txt](api35/window/home-applight-sysdark.txt)
- [api35/window/home-dark-grep.txt](api35/window/home-dark-grep.txt)
- [api35/window/home-dark.txt](api35/window/home-dark.txt)
- [api35/window/home-gesture-light-grep.txt](api35/window/home-gesture-light-grep.txt)
- [api35/window/home-gesture-light.txt](api35/window/home-gesture-light.txt)
- [api35/window/home-light-grep.txt](api35/window/home-light-grep.txt)
- [api35/window/home-light.txt](api35/window/home-light.txt)
- [api35/window/land270-catalog-grep.txt](api35/window/land270-catalog-grep.txt)
- [api35/window/land270-catalog.txt](api35/window/land270-catalog.txt)
- [api35/window/land90-home-grep.txt](api35/window/land90-home-grep.txt)
- [api35/window/land90-home.txt](api35/window/land90-home.txt)
- [api35/window/onboarding-grep.txt](api35/window/onboarding-grep.txt)
- [api35/window/onboarding.txt](api35/window/onboarding.txt)
