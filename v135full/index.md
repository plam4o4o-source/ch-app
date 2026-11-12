# Full emulator test of the released v1.2.35 (aef8910, versionCode 1507364); v1.2.34 (3812d8e, 1504705) where a part compares

debug/a6-full (runs in runs.txt, latest 43312002128 @ 1a8b86a). API 33 google_apis x86_64, -gpu swangle_indirect (software GPU) unless the part says otherwise (hi28, c28: API 28; c26: API 26; c36: API 36). Built prodRelease (minified) x86_64 with a throwaway key; year35: devDebug. Screenshots are JPEG, scaled down (sheets/ = contact sheets). Earlier: [../v135/index.md](../v135/index.md), [../v134/index.md](../v134/index.md).

| part | status |
|---|---|
| a11yt | COMPLETE; part=a11yt target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| a6new | COMPLETE; part=a6new target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| c26 | COMPLETE; part=c26 target=google_apis api=26 gpu=swangle_indirect emulator-step=success |
| c28 | COMPLETE; part=c28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| c36 | COMPLETE; part=c36 target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| crash26 | COMPLETE; part=crash26 target=google_apis api=26 gpu=swangle_indirect emulator-step=success |
| gfx35 | COMPLETE; part=gfx35 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| hi28 | COMPLETE; part=hi28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| idle33 | COMPLETE; part=idle33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| land35 | COMPLETE; part=land35 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| mem | COMPLETE; part=mem target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| monkey | COMPLETE; part=monkey target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| offline | COMPLETE; part=offline target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| robust | COMPLETE; part=robust target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| tour-a | COMPLETE; part=tour-a target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| tour-b | COMPLETE; part=tour-b target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| tour-c | COMPLETE; part=tour-c target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| upd35 | COMPLETE; part=upd35 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| year35 | COMPLETE; part=year35 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| yup35 | COMPLETE; part=yup35 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |

```
1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json
1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/update/43312002128/update.json
devDebug 1.2.35 aef8910 (org.chyavorec.app.dev, demo sign-in DEMO-0001)
1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest)
app.apk 4317894 bytes
app-base.apk 4316418 bytes
app-base-upd.apk 4316454 bytes
app-dev.apk 27866998 bytes
```

## a11yt

```
part a11yt; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4222 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## AT. Test accessibility service with touch exploration (TalkBack stand-in): what a screen reader gets
A11Y service: 11-12 19:30:18.763  7401  7401 I PCA11Y  : connected flags=80
touch exploration true: touchExplorationEnabled=true
TREE home (131 lines):
TREE nodes=128 windows: ['11-12 19:30:31.991  7401  7401 I PCDUMP  : thome W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:30:32.001  7401  7401 I PCDUMP  : thome W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=15 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Начало']; headings: ['', 'Предстоящи събития']
TREE nodes with labelled (custom) actions: 2: 'Последни новини' [2131361799:Следваща новина;2131361800:Предишна новина] | '' [2131361799:Следваща новина;2131361800:Предишна новина]
TREE carousel nodes 'Последни новини': 1; nodes with Следваща/Предишна новина: 2: 'Последни новини' clk=false b=[0,714][1080,1337] acts=[2131361799:Следваща новина;2131361800:Предишна новина] | '' clk=true b=[126,714][954,1337] acts=[2131361799:Следваща новина;2131361800:Предишна новина]
TREE catalog (150 lines):
TREE nodes=147 windows: ['11-12 19:30:44.536  7401  7401 I PCDUMP  : tcatalog W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:30:44.539  7401  7401 I PCDUMP  : tcatalog W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=22 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Каталог']; headings: ['Каталог']
TREE nodes with labelled (custom) actions: 0: 
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
TREE surprise (32 lines):
TREE nodes=29 windows: ['11-12 19:30:53.301  7401  7401 I PCDUMP  : tsurprise W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:30:53.305  7401  7401 I PCDUMP  : tsurprise W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=3 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Предложение']; headings: []
TREE nodes with labelled (custom) actions: 0: 
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
TREE book (62 lines):
TREE nodes=59 windows: ['11-12 19:31:03.077  7401  7401 I PCDUMP  : tbook W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:31:03.077  7401  7401 I PCDUMP  : tbook W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=4 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', '12 -те вятъра']; headings: ['12 -те вятъра', 'Библиографски данни']
TREE nodes with labelled (custom) actions: 3: '' [16:Търси книги от автора] | '' [32:Копирай] | '' [32:Копирай]
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
TREE book-share-sheet (29 lines):
TREE nodes=26 windows: ['11-12 19:31:09.896  7401  7401 I PCDUMP  : tbook-share-sheet W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:31:09.953  7401  7401 I PCDUMP  : tbook-share-sheet W1 type=1 active=true focused=true title= pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=4 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Долен лист']; headings: ['Сподели']
TREE nodes with labelled (custom) actions: 2: '' [32:показване на подсказка] | '' [1048576:Отхвърляне на долния лист]
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
TREE article (48 lines):
TREE nodes=45 windows: ['11-12 19:31:23.210  7401  7401 I PCDUMP  : tarticle W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:31:23.213  7401  7401 I PCDUMP  : tarticle W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=6 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026']; headings: ['Групата за народни танци при ч']
TREE nodes with labelled (custom) actions: 1: '' [16:Отвори снимката в цял екран]
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
TREE article-scrolled (48 lines):
TREE nodes=45 windows: ['11-12 19:31:27.902  7401  7401 I PCDUMP  : tarticle-scrolled W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:31:27.905  7401  7401 I PCDUMP  : tarticle-scrolled W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=6 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026']; headings: ['Групата за народни танци при ч']
TREE nodes with labelled (custom) actions: 1: '' [16:Отвори снимката в цял екран]
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
TREE reader-sheet (45 lines):
TREE nodes=42 windows: ['11-12 19:31:34.518  7401  7401 I PCDUMP  : treader-sheet W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:31:34.524  7401  7401 I PCDUMP  : treader-sheet W1 type=1 active=true focused=true title= pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=6 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Долен лист']; headings: ['Режим за четене']
TREE nodes with labelled (custom) actions: 2: '' [32:показване на подсказка] | '' [1048576:Отхвърляне на долния лист]
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
TREE event (47 lines):
TREE nodes=44 windows: ['11-12 19:31:57.278  7401  7401 I PCDUMP  : tevent W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:31:57.280  7401  7401 I PCDUMP  : tevent W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=7 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', '80 г. от създаването на ЮНЕСКО']; headings: ['80 г. от създаването на ЮНЕСКО']
TREE nodes with labelled (custom) actions: 1: '' [16:покажи мястото на картата]
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
TREE viewer (36 lines):
TREE nodes=33 windows: ['11-12 19:32:26.254  7401  7401 I PCDUMP  : tviewer W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:32:26.256  7401  7401 I PCDUMP  : tviewer W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=4 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Снимки: Фотодокументална изложба']; headings: []
TREE nodes with labelled (custom) actions: 0: 
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
TREE card (54 lines):
TREE nodes=51 windows: ['11-12 19:32:40.241  7401  7401 I PCDUMP  : tcard W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:32:40.383  7401  7401 I PCDUMP  : tcard W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=7 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Читателска карта']; headings: []
TREE nodes with labelled (custom) actions: 0: 
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
TREE settings (87 lines):
TREE nodes=84 windows: ['11-12 19:32:59.412  7401  7401 I PCDUMP  : tsettings W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:32:59.414  7401  7401 I PCDUMP  : tsettings W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=10 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Настройки']; headings: ['Изглед']
TREE nodes with labelled (custom) actions: 0: 
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
touch exploration false: touchExplorationEnabled=false
## AT2. Unlabelled / tiny targets as the service sees them (all trees)
== a11ytree-article-scrolled
TREE nodes=45 windows: ['11-12 19:31:27.902  7401  7401 I PCDUMP  : tarticle-scrolled W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:31:27.905  7401  7401 I PCDUMP  : tarticle-scrolled W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=6 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026']; headings: ['Групата за народни танци при ч']
TREE nodes with labelled (custom) actions: 1: '' [16:Отвори снимката в цял екран]
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
== a11ytree-article
TREE nodes=45 windows: ['11-12 19:31:23.210  7401  7401 I PCDUMP  : tarticle W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:31:23.213  7401  7401 I PCDUMP  : tarticle W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=6 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026']; headings: ['Групата за народни танци при ч']
TREE nodes with labelled (custom) actions: 1: '' [16:Отвори снимката в цял екран]
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
== a11ytree-book-share-sheet
TREE nodes=26 windows: ['11-12 19:31:09.896  7401  7401 I PCDUMP  : tbook-share-sheet W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:31:09.953  7401  7401 I PCDUMP  : tbook-share-sheet W1 type=1 active=true focused=true title= pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=4 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Долен лист']; headings: ['Сподели']
TREE nodes with labelled (custom) actions: 2: '' [32:показване на подсказка] | '' [1048576:Отхвърляне на долния лист]
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
== a11ytree-book
TREE nodes=59 windows: ['11-12 19:31:03.077  7401  7401 I PCDUMP  : tbook W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:31:03.077  7401  7401 I PCDUMP  : tbook W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=4 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', '12 -те вятъра']; headings: ['12 -те вятъра', 'Библиографски данни']
TREE nodes with labelled (custom) actions: 3: '' [16:Търси книги от автора] | '' [32:Копирай] | '' [32:Копирай]
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
== a11ytree-card
TREE nodes=51 windows: ['11-12 19:32:40.241  7401  7401 I PCDUMP  : tcard W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:32:40.383  7401  7401 I PCDUMP  : tcard W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=7 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Читателска карта']; headings: []
TREE nodes with labelled (custom) actions: 0: 
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
== a11ytree-catalog
TREE nodes=147 windows: ['11-12 19:30:44.536  7401  7401 I PCDUMP  : tcatalog W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:30:44.539  7401  7401 I PCDUMP  : tcatalog W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=22 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Каталог']; headings: ['Каталог']
TREE nodes with labelled (custom) actions: 0: 
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
== a11ytree-event
TREE nodes=44 windows: ['11-12 19:31:57.278  7401  7401 I PCDUMP  : tevent W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:31:57.280  7401  7401 I PCDUMP  : tevent W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=7 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', '80 г. от създаването на ЮНЕСКО']; headings: ['80 г. от създаването на ЮНЕСКО']
TREE nodes with labelled (custom) actions: 1: '' [16:покажи мястото на картата]
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
== a11ytree-home
TREE nodes=128 windows: ['11-12 19:30:31.991  7401  7401 I PCDUMP  : thome W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:30:32.001  7401  7401 I PCDUMP  : thome W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=15 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Начало']; headings: ['', 'Предстоящи събития']
TREE nodes with labelled (custom) actions: 2: 'Последни новини' [2131361799:Следваща новина;2131361800:Предишна новина] | '' [2131361799:Следваща новина;2131361800:Предишна новина]
TREE carousel nodes 'Последни новини': 1; nodes with Следваща/Предишна новина: 2: 'Последни новини' clk=false b=[0,714][1080,1337] acts=[2131361799:Следваща новина;2131361800:Предишна новина] | '' clk=true b=[126,714][954,1337] acts=[2131361799:Следваща новина;2131361800:Предишна новина]
== a11ytree-reader-sheet
TREE nodes=42 windows: ['11-12 19:31:34.518  7401  7401 I PCDUMP  : treader-sheet W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:31:34.524  7401  7401 I PCDUMP  : treader-sheet W1 type=1 active=true focused=true title= pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=6 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Долен лист']; headings: ['Режим за четене']
TREE nodes with labelled (custom) actions: 2: '' [32:показване на подсказка] | '' [1048576:Отхвърляне на долния лист]
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
== a11ytree-settings
TREE nodes=84 windows: ['11-12 19:32:59.412  7401  7401 I PCDUMP  : tsettings W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:32:59.414  7401  7401 I PCDUMP  : tsettings W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=10 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Настройки']; headings: ['Изглед']
TREE nodes with labelled (custom) actions: 0: 
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
== a11ytree-surprise
TREE nodes=29 windows: ['11-12 19:30:53.301  7401  7401 I PCDUMP  : tsurprise W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:30:53.305  7401  7401 I PCDUMP  : tsurprise W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=3 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Предложение']; headings: []
TREE nodes with labelled (custom) actions: 0: 
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
== a11ytree-viewer
TREE nodes=33 windows: ['11-12 19:32:26.254  7401  7401 I PCDUMP  : tviewer W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-12 19:32:26.256  7401  7401 I PCDUMP  : tviewer W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=4 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Снимки: Фотодокументална изложба']; headings: []
TREE nodes with labelled (custom) actions: 0: 
TREE carousel nodes 'Последни новини': 0; nodes with Следваща/Предишна новина: 0: 
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 1 | Първи кадър: медиана 2674 мс, последен 2674 мс | Пълно показване: медиана 2674 мс, последно 2674 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 1" text="Първи кадър: медиана 2674 мс, последен 2674 мс" text="Пълно показване: медиана 2674 мс, последно 2674 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['5192']): 63 in 10 groups
      46  W/AccessibilityDelegate: Drawing order is not available, was AccessibilityNodeInfo requested for a child node before its parent?
       3  W/Parcel: Expecting binder but got null!
       3  E/OpenGLRenderer: Unable to match the desired swap behavior.
       2  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       2  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       2  W/g.chyavorec.app: Suspending all threads took: #.#ms
       2  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
       1  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       1  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       1  W/Gralloc4: allocator #.x is not supported
contact sheets: 1 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5192']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 13
   skipped-frames events=13 max=59 total=541
== Davey: 20
   davey events=20 max=1759ms median=1034ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## a6new

```
part a6new; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 7172 ms
SENSORS: Accelerometer (handle=0x00000000, connectio;Accelerometer Uncalibrated ;Game Rotation Vector Sensor ;Gyroscope ;Gyroscope (uncalibrated) ;Gyroscope Bias (debug)    ;Gyroscope Sensor ;Magnetic field sensor ;Magnetic field sensor (uncalibrated) ;Rotation Vector Sensor ;Rotation Vector Sensor    ;accel
emu console: sensor status → acceleration: enabled. gyroscope: enabled. 
## N1. Card added by hand (Моето › Добави читателска карта)
ADD by hand: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## N2. Living шевица on Home: animates after a cold start, stops later (0 frames at rest)
IDLE home-new-a-76-91s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7869
RIBBON new-a [0 280 1080 398]: diff 0.5→3 s 4.11; 3→6 s 5.05; 70→75 s 0.00 (0 = still); pattern at 0.5 s: mean=209.5 std=67.1 thread-px=26.8%; at 75 s: mean=209.6 std=67.0 thread-px=26.7%
## B1. Home ribbon „жива шевица“ (AGSL, API 33): renders, sweeps ~24 s, then stops
RIBBON frames in the first 34 s: 138
RIBBON box [0 280 1080 398] at 03 s: mean=209.5 std=67.1 thread-px=26.8%
RIBBON box [0 280 1080 398] at 12 s: mean=209.4 std=67.0 thread-px=26.8%
RIBBON box [0 280 1080 398] at 34 s: mean=209.6 std=67.0 thread-px=26.7%
RIBBON change between shots (3,5,8,12,16,20,24,28,34 s): 0.79 4.02 4.02 4.30 4.61 4.62 3.95 0.22
RIBBON still after the budget (34 s → 40 s): diff 0.00
## B1b. The first scroll stops the ribbon (new process)
RIBBON after the first scroll: change a→b→c 0.00 0.00 (≈0 = stopped)
## B1c. Once per process: back to Home from another tab — no new sweep
RIBBON after a tab round trip: change 43.25
RIBBON shader errors in logcat: 1
## N3. Card screen: frames/CPU at rest, tilt (foil)
IDLE card-0-15s (15 s, no input): frames +84; cpu=29.5% (ticks 443 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=28.5%, g.chyavorec.app=0.5%, DefaultExecutor=0.2%, Jit thread pool=0.1%, HeapTaskDaemon=0.1%; pid 8705
SENSORS card-15s: lines with the app 0: 
IDLE card-30-45s (15 s, no input): frames +0; cpu=0.1% (ticks 1 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 8705
IDLE card-45-60s (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 8705
SENSORS card-60s: lines with the app 0: 
SENSORS card-woken: lines with the app 0: 
TILT card: noise rest→rest-b 0.00; rest→accA 3.15; accA→accB 2.30; accB→back 0.00
IDLE card-after-tilt-20-35s (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 8705
SENSORS card-after-tilt: lines with the app 0: 
## N4. Fullscreen card: frames/CPU at rest, tilt, flip
IDLE card-full-0-15s (15 s, no input): frames +82; cpu=27.1% (ticks 406 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=26.3%, g.chyavorec.app=0.5%, DefaultExecutor=0.1%, binder:8705_6=0.1%, Jit thread pool=0.1%; pid 8705
SENSORS card-full-15s: lines with the app 0: 
IDLE card-full-30-45s (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 8705
IDLE card-full-45-60s (15 s, no input): frames +0; cpu=0.1% (ticks 1 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 8705
SENSORS card-full-60s: lines with the app 0: 
fullscreen card reached: yes
SENSORS card-full-woken: lines with the app 0: 
TILT card-full: noise rest→rest-b 0.00; rest→accA 0.00; accA→accB 2.07; accB→back 0.00
IDLE card-full-after-tilt-20-35s (15 s, no input): frames +0; cpu=0.1% (ticks 1 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 8705
SENSORS card-full-after-tilt: lines with the app 0: 
IDLE card-full-flip-2-17s (15 s, no input): frames +0; cpu=0.2% (ticks 3 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.2%; pid 8705
after back from the fullscreen card: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" 
fullscreen card opened in landscape: NO
## N5. „Изненадай ме“: dice, shake, back
## B12. 'Изненадай ме': the dice button in the catalogue
REC surprise-dice: 42 frames @8 fps; motion all: frames=42 moving-frames(>1.0)=2 max-step=196.1 max-vs-first=201.0 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.1 0.0 0.0 0.0 0.0 4.6 0.0 0.0 0.0 0.0 196.1 0.0 0.0 0.0 0.0 0.0 0.3]
SURPRISE dice: text="Още една" text="Към книгата" ; bottom bar hidden
SURPRISE 'Към книгата' → book screen yes: text="Легенда за реката" text="Петър Колев" text="Налична" 
## B13. 'Изненадай ме' by shaking (emulator accelerometer: two peaks > 2.3 g within 0.7 s)
accelerometer: 0000000000) Goldfish 3-axis Accelerometer | The Android Open Source Project | ver: 1 | type: android.sensor.acceleromete
SHAKE one short peak (info; a peak held > 120 ms counts twice by design): not triggered
SHAKE double peak: TRIGGERED; text="Към книгата" 
## B13b. Shake on another tab (catalogue not resumed) must not trigger
on Home before the shake: TAB selected: ['Начало']
SHAKE on Home: not triggered
SURPRISE open: text="Още една" text="Към книгата" 
SURPRISE 'Още една': content-desc="Предложение: Гласът на любовта — Симеон Василев" text="Още една" text="Към книгата" content-desc="Затвори" 
SURPRISE after the back key: closed; catalogue shown; tab bar visible
## N6. Share as image: an article, a book, an event (the files are in share/)
SHARE art: chooser OPEN after 2 s; cache/share: 296537 2026-11-12 19:42 chyavorec-news-1794512565135.jpg;
SHARE art file: chyavorec-news-1794512565135.jpg 296537 B; JPEG (1080, 1350) RGB
SHARE art received by the helper: action=android.intent.action.SEND type=image/jpeg flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-news-1794512565135.jpg mime=image/jpeg bytes=296537 png=false dims=1080x1350 subject=Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026 text=Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026 https://chyavorec.org/news/grupata-za-narodni-tantsi-pri-chitalishteto-ff-vrabevo-v-ritmi-2026
SHARE book: chooser OPEN after 3 s; cache/share: 774403 2026-11-12 19:43 chyavorec-book-399560-1794512603882.png;
SHARE book file: chyavorec-book-399560-1794512603882.png 774403 B; PNG (1080, 1920) RGBA
SHARE book received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794512603882.png mime=image/png bytes=774403 png=true dims=1080x1920 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
SHARE book2: chooser OPEN after 2 s; cache/share: 790424 2026-11-12 19:43 chyavorec-book-1-1794512638472.png;
SHARE book2 file: chyavorec-book-1-1794512638472.png 790424 B; PNG (1080, 1920) RGBA
SHARE book2 received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-1-1794512638472.png mime=image/png bytes=790424 png=true dims=1080x1920 subject=Дългият любовта text=Дългият любовта Пенка Христова В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=1
SHARE ev1-new: chooser OPEN after 2 s; cache/share: 654869 2026-11-12 19:44 chyavorec-event-1794512686986.png;
SHARE ev1-new file: chyavorec-event-1794512686986.png 654869 B; PNG (1080, 1920) RGBA
SHARE ev1-new: chyavorec-event-1794512686986.png (no photo?) text="80 г. от създаването на ЮНЕСКО" text="16 ноември 2026" 
SHARE ev2-new: chooser OPEN after 2 s; cache/share: 648784 2026-11-12 19:45 chyavorec-event-1794512728140.png;
SHARE ev2-new file: chyavorec-event-1794512728140.png 648784 B; PNG (1080, 1920) RGBA
SHARE ev2-new: chyavorec-event-1794512728140.png (no photo?) text="Панайот Пипков · Ден на семейството" text="21 ноември 2026" 
SHARE ev3-new: chooser OPEN after 2 s; cache/share: 643429 2026-11-12 19:46 chyavorec-event-1794512769082.png;
SHARE ev3-new file: chyavorec-event-1794512769082.png 643429 B; PNG (1080, 1920) RGBA
SHARE ev3-new: chyavorec-event-1794512769082.png (no photo?) text="Световен ден за борба срещу СПИН" text="1 декември 2026" 
SHARE ev4-new: chooser OPEN after 2 s; cache/share: 645003 2026-11-12 19:46 chyavorec-event-1794512809955.png;
SHARE ev4-new file: chyavorec-event-1794512809955.png 645003 B; PNG (1080, 1920) RGBA
SHARE ev4-new: chyavorec-event-1794512809955.png (no photo?) text="Международен ден на инвалида" text="3 декември 2026" 
SHARE new: event with a photo: none of the first 4
SHARE art-dark: chooser OPEN after 1 s; cache/share: 296537 2026-11-12 19:47 chyavorec-news-1794512842085.jpg;
SHARE art-dark file: chyavorec-news-1794512842085.jpg 296537 B; JPEG (1080, 1350) RGB
SHARE art-dark received by the helper: action=android.intent.action.SEND type=image/jpeg flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-news-1794512842085.jpg mime=image/jpeg bytes=296537 png=false dims=1080x1350 subject=Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026 text=Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026 https://chyavorec.org/news/grupata-za-narodni-tantsi-pri-chitalishteto-ff-vrabevo-v-ritmi-2026
SHARE book-dark: chooser OPEN after 2 s; cache/share: 774403 2026-11-12 19:47 chyavorec-book-399560-1794512873877.png;
SHARE book-dark file: chyavorec-book-399560-1794512873877.png 774403 B; PNG (1080, 1920) RGBA
SHARE book-dark received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794512873877.png mime=image/png bytes=774403 png=true dims=1080x1920 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
## N7. Settings › Обновления
UPDATES card light: text="Обновления" text="Автоматично обновяване" text="Провери за нова версия" 
UPDATES after 'Провери за нова версия': text="Имаш последната версия (1.2.35)" 
## N8. Catalogue: „Само налични“, sort, shelf
## C1. Catalogue: search, filter 'Само налични', sort, shelf view
search '12': 108 results
FILTERS sheet: content-desc="Манипулатор за преместване с плъзгане" text="Филтри" text="Без филтри" text="Само налични" text="Вид документ" text="книга" text="видеодокумент" text="картографско издание" text="друго" text="електронен документ" text="графично издание" text="аудиодокумент" text="продължаващо издание" text="патент/стандарт" text="нотно издание" text="Раздел (жанр)" text="Общ отдел" text="Философия. П
FILTER 'Само налични': 108 → 98 results; chips: 
SORT options: text="Най-подходящи  ✓" text="Заглавие (А–Я)" text="Автор (А–Я)" text="Година (нови първо)" text="Година (стари първо)" text="Нови постъпления" 
after sort by year: text="12" content-desc="Изчисти" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="12 -те вятъра" text="98 резултата  ·  данни от InvLib към 09.11.2026" text="Дъхът на любовта" 
GFX shelf-scroll frames=   50  janky=50 (100.00%)  p50=300ms p90=450ms p95=450ms p99=550ms  slowUI=13 slowDraw=50 highInput=0
catalogue after the filter (state kept?): text="12" content-desc="Изчисти" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="12 -те вятъра" text="98 резултата  ·  данни от InvLib към 09.11.2026" text="Дъхът на любовта" 
## N9. Pull-to-refresh rosette
## PR. Pull-to-refresh rosette: slow 1500 ms pull from the top, screencaps on the device mid-gesture
REFRESH news: top-area diff vs before (mid-pull 1,2,3 | released 4 | +0.5 s 5 | +1.7 s 6): 0.00 0.00 0.00 0.00 0.00 0.00 
REFRESH home: top-area diff vs before (mid-pull 1,2,3 | released 4 | +0.5 s 5 | +1.7 s 6): 0.04 0.58 0.00 0.02 0.02 0.02 
REFRESH calendar: top-area diff vs before (mid-pull 1,2,3 | released 4 | +0.5 s 5 | +1.7 s 6): 0.00 0.00 0.00 0.00 0.00 0.00 
REFRESH my: top-area diff vs before (mid-pull 1,2,3 | released 4 | +0.5 s 5 | +1.7 s 6): 0.00 0.00 0.00 0.00 0.00 0.00 
REFRESH gallery: top-area diff vs before (mid-pull 1,2,3 | released 4 | +0.5 s 5 | +1.7 s 6): 0.16 0.54 0.00 0.00 0.00 0.00 
REFRESH catalog: top-area diff vs before (mid-pull 1,2,3 | released 4 | +0.5 s 5 | +1.7 s 6): 0.00 0.00 0.00 0.00 0.00 0.00 
## PR2. The same pull on News, recorded (screenrecord, 8 fps)
REC refresh-news: screenrecord gave no frames
REFRESH news-dark: top-area diff vs before (mid-pull 1,2,3 | released 4 | +0.5 s 5 | +1.7 s 6): 0.42 0.36 0.47 0.00 0.00 0.00 
contact sheets: 14 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 5 | Първи кадър: медиана 2891 мс, последен 3657 мс | Пълно показване: медиана 3957 мс, последно 4703 мс | Топъл старт · 5 | Първи кадър: медиана 152 мс, последен 85 мс | Насечени кадри по екрани | article: 13,2 % от 53 кадъра · най-бавен 765 мс
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 5" text="Първи кадър: медиана 2891 мс, последен 3657 мс" text="Пълно показване: медиана 3957 мс, последно 4703 мс" text="Топъл старт · 5" text="Първи кадър: медиана 152 мс, последен 85 мс" text="Насечени кадр
APP W/E lines (pids ['5394', '7869', '8284', '8494', '8705']): 100 in 15 groups
      26  W/Parcel: Expecting binder but got null!
      26  E/OpenGLRenderer: Unable to match the desired swap behavior.
       9  E/FrameEvents: updateAcquireFence: Did not find frame.
       5  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       5  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       5  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       5  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       5  W/Gralloc4: allocator #.x is not supported
       4  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
       4  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       2  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#cd#d-#-#f#-#dbc-c#dce#fb, generation=#)
       1  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
       1  W/RemoteInputConnectionImpl: setComposingRegion on inactive InputConnection
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object g#.s(java.lang.Object)(r
       1  W/g.chyavorec.app: Cleared Reference was only reachable from finalizer (only reported once)
contact sheets: 15 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5394', '7869', '8284', '8494', '8705']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 74
   skipped-frames events=74 max=95 total=3202
== Davey: 156
   davey events=156 max=2814ms median=880ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## c26

```
part c26; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15982 MB RAM
android 8.0.0 sdk 26; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-v26
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 1899 ms
## C1. Tour of all screens on API 26
## T-api26-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api26-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api26-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · СЪБИТИЕ · С хоро и песен във Водица · 26 септември 2026 · Сканирай · Читателска карта
CAP api26-light/01c-home-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | КАЛЕНДАР · Предстоящи събития · Всички · 16 · НОЕ · 80 г. от създаването на ЮНЕС
CAP api26-light/01d-home-4: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP api26-light/01e-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
WARN: tab bar not reached
CAP api26-light/02-catalog: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=1 [Търси книга, новина, събитие…] | Лента за търсене · Назад · Търси книга, новина, събитие · Сканирай баркод · Всички · Книги
CAP api26-light/02g-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP api26-light/03-book: targets=0 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: OTHER org.chyavorec.app | texts=2 ellipsized=0 | Читалище Яворец спря · Повторно отваряне на приложе
CAP api26-light/03b-book-2: targets=0 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: OTHER com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity | texts=16 ellipsized=0 | 12 ноември · 12 ноември · ЧЕТВЪРТЪК, 2026 Г. · четвъртък, 2026 г. · Maps · Maps
CAP api26-light/03c-book-3: targets=0 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: OTHER com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity | texts=55 ellipsized=0 | Търсене в приложенията · YouTube · YouTube · Chrome · Chrome · Gmail
CAP api26-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Вярност в смърт · Още една · Към книгата · Затвори
CAP api26-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CAP api26-light/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP api26-light/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api26-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api26-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api26-light/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-light/08b-news-2: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=4 | front: app | texts=28 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api26-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api26-light/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Панайот Пипков · Ден на семе · 21 ноември 2026 · Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана
CAP api26-light/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Панайот Пипков · Ден на семе · 21 ноември 2026 · Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана
CAP api26-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-light/12-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api26-light/12b-more-2: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия · Снимки от сайта
CAP api26-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api26-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api26-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · Сподели · Отвори в сайта · За снимката
CAP api26-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · 70-те години · Сподели · Отвори в сайта
CAP api26-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api26-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP api26-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-light/18-settings: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP api26-light/18b-settings-2: targets=9 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п
CAP api26-light/18c-settings-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.35
CAP api26-light/18d-settings-4: targets=9 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Корици по ISBN от covers.ope · Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни · Новините, събитията, каталог · Поверителност
CAP api26-light/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api26-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api26-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api26-light/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api26-light/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api26-light/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api26-light/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api26-light/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 3
CAP api26-light/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 3
CAP api26-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP api26-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api26-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api26-light: A11Y AGG: 52 screens analysed, 0 dumps failed; totals MISSING=5 DOUBLE=0 SAME-LABEL=3 SMALL(<48dp)=12
app theme → =Тъмна|=Dark: text="Изглед" text="Тема" text="Тъмна" text="Размер на текста" 
## T-api26-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api26-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api26-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · СЪБИТИЕ · С хоро и песен във Водица · 26 септември 2026 · Моите книги · Читателска карта
CAP api26-dark/01c-home-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | КАЛЕНДАР · Предстоящи събития · Всички · 16 · НОЕ · 80 г. от създаването на ЮНЕС
CAP api26-dark/01d-home-4: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP api26-dark/02-catalog: targets=22 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api26-dark/02b-filters: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=0 | Манипулатор за преместване с · продължаващо издание · патент/стандарт · нотно издание · Раздел (жанр) · Общ отдел
CAP api26-dark/02c-filters-2: targets=23 MISSING=2 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=3 | front: app | texts=27 ellipsized=0 | Манипулатор за преместване с · Раздел (жанр) · Общ отдел · Философия. Психология · Религия · Обществени науки
CAP api26-dark/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api26-dark/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api26-dark/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api26-dark/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=44 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api26-dark/03-book: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api26-dark/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api26-dark/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api26-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Тишината на мор · Още една · Към книгата · Затвори
CAP api26-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP api26-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP api26-dark/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api26-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api26-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api26-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-dark/08b-news-2: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api26-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api26-dark/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Панайот Пипков · Ден на семе · 21 ноември 2026 · Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана
CAP api26-dark/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Панайот Пипков · Ден на семе · 21 ноември 2026 · Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана
CAP api26-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-dark/12-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api26-dark/12b-more-2: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия · Снимки от сайта
CAP api26-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api26-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api26-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · Сподели · Отвори в сайта · За снимката
CAP api26-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · 70-те години · Сподели · Отвори в сайта
CAP api26-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api26-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP api26-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-dark/18-settings: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Тъмна · Размер на текста · Стандартен · Палитра
CAP api26-dark/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP api26-dark/18c-settings-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.35
CAP api26-dark/18d-settings-4: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Корици по ISBN от covers.ope · Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни · Новините, събитията, каталог · Поверителност
CAP api26-dark/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api26-dark/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api26-dark/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api26-dark/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api26-dark/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api26-dark/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api26-dark/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api26-dark/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 4
CAP api26-dark/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 4
CAP api26-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP api26-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api26-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api26-dark: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=5 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=51
app theme → =Както системата|=System default: text="Изглед" text="Тема" text="Както системата" text="Размер на текста" 
A11Y AGG: 108 screens analysed, 0 dumps failed; totals MISSING=10 DOUBLE=0 SAME-LABEL=8 SMALL(<48dp)=63
== MISSING: 8 distinct
   View@[0,1906][1080,2032]  [1 screens: 02-catalog]
   View@[53,127][529,190]  [1 screens: 02c-filters-2]
   View@[550,127][929,190]  [1 screens: 02c-filters-2]
   View@[42,2256][524,2274]  [1 screens: 13-gallery]
   View@[556,2256][1038,2274]  [1 screens: 13-gallery]
   View@[731,0][1069,2]  [1 screens: 01d-home-4]
   View@[0,2252][1080,2274]  [1 screens: 18b-settings-2]
   View@[0,2262][1080,2274]  [1 screens: 18d-settings-4]
== SMALL: 24 distinct
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   ''  [2 screens: 02c-filters-2, 13-gallery]
   'Бевълстоук : Кн. 1 - 2, Куин, '  [2 screens: 02e-shelf, 02f-shelf-2]
   'Безсрамна добродетел, Робъртс,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият дома, Николай Симеонов,'  [2 screens: 02e-shelf, 02f-shelf-2]
   '12 -те вятъра, Гарфилд, Джудит'  [1 screens: 02e-shelf]
   'Аз обичам Ню Йорк, Келк, Линдз'  [1 screens: 02e-shelf]
   'Ако върнеш времето назад, Потъ'  [1 screens: 02e-shelf]
   'Алтъна, Никова, Ивелина'  [1 screens: 02e-shelf]
   'Анатомът, Андахази, Федерико'  [1 screens: 02e-shelf]
   'Белият звездите, Диана Христов'  [1 screens: 02e-shelf]
   'Всичко'  [1 screens: 02f-shelf-2]
   'Заглавие'  [1 screens: 02f-shelf-2]
   'Автор'  [1 screens: 02f-shelf-2]
   'ISBN'  [1 screens: 02f-shelf-2]
   'Белият вятъра, Петър Здравков'  [1 screens: 02f-shelf-2]
   'Белият детството, Симеон Колев'  [1 screens: 02f-shelf-2]
   'Запази: Фолклорно пътешествие '  [1 screens: 08-news]
   'Всички'  [1 screens: 08b-news-2]
## C2. Home ribbon and the card on API 26
IDLE card26-3-18s (15 s, no input): frames +0; cpu=1.7% (ticks 26 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=1.4%, DefaultExecutor=0.3%, Jit thread pool=0.1%; pid 16904
CARD fullscreen on API 26: yes; app in front yes
IDLE cardfull26-30-45s (15 s, no input): frames +0; cpu=0.1% (ticks 1 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 16904
## C3. Surprise ×2, share a book as an image on API 26
SURPRISE 1: text="Още една" text="Към книгата" ; app in front yes
SURPRISE 2: text="Още една" text="Към книгата" ; app in front yes
SHARE book26: chooser OPEN after 3 s; cache/share: 2159967 2026-11-12 21:13 chyavorec-book-399560-1794518027351.png;
SHARE book26 file: chyavorec-book-399560-1794518027351.png 2159967 B; PNG (1080, 1920) RGBA
SHARE book26 received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794518027351.png mime=image/png bytes=2159967 png=true dims=1080x1920 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
## C5. Rotation and process death on API 26 (book, catalogue, article)
ROT book: pid 16904→16904 (same); landscape: kept 38% of 24 texts; lost e.g. ['Вид', 'Език', 'Отдел', 'книга']; back to portrait: kept 100% of 24 texts
PROCDEATH book: killed NO (16904); new pid 16904; restored: kept 100% of 24 texts; front app
ROT catalog: pid 16904→16904 (same); landscape: kept 47% of 34 texts; lost e.g. ['Изчисти', 'Белият дома', 'Белият пътя', 'Елена Русева']; back to portrait: kept 47% of 34 texts; lost e.g. ['Изчисти', 'Белият пътя', 'Белият дома', 'Мартин Тонев']
PROCDEATH catalog: killed NO (16904); new pid 16904; restored: kept 47% of 34 texts; lost e.g. ['Изчисти', 'Белият пътя', 'Белият дома', 'Мартин Тонев']; front app
ROT article: pid 16904→16904 (same); landscape: kept 78% of 9 texts; lost e.g. ['СЪБИТИЕ  ·  25 юли 2026', 'Групата за народни танци при Н']; back to portrait: kept 100% of 9 texts
PROCDEATH article: killed NO (16904); new pid 16904; restored: kept 100% of 9 texts; front app
## C5b. Scanner screen: manual entry and back ×2, repeated ×8 (the API 26 crash came right after this in round 1)
SCAN loop 1: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 2: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 3: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 4: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 5: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 6: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 7: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 8: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
## C6. Monkey 2000 (seed 1357) on API 26
## W. First launch and warm-up
first-install catalogue results after 1570 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=19691
MONKEY exit=0 duration=75 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '19691'; airplane=0; camera: 
MEM after-monkey (raw) PSS=101179KB  Java=17052KB  Native=50212KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=?KB  Views=16 Activities=1
MEM after-monkey (trimmed) PSS=92238KB  Java=8328KB  Native=50220KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=?KB  Views=16 Activities=1
## C6. Monkey 2000 (seed 4242) on API 26
## W. First launch and warm-up
first-install catalogue results after 1636 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=20167
MONKEY exit=0 duration=87 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '20167'; airplane=0; camera: 
MEM after-monkey (raw) PSS=83354KB  Java=16356KB  Native=34428KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=?KB  Views=26 Activities=1
MEM after-monkey (trimmed) PSS=76855KB  Java=10092KB  Native=34440KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=?KB  Views=24 Activities=1
## C6. Monkey 2000 (seed 777) on API 26
## W. First launch and warm-up
first-install catalogue results after 2576 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=20641
MONKEY exit=148 duration=8 s; ** Monkey aborted due to error. Events injected: 148 
MONKEY markers: CRASH=1 ANR=0 FATAL=0
pid after monkey: '20641'; airplane=0; camera: 
MEM after-monkey (raw) PSS=71445KB  Java=13408KB  Native=30632KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=?KB  Views=16 Activities=1
MEM after-monkey (trimmed) PSS=68696KB  Java=10448KB  Native=30656KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=14)  RSS=?KB  Views=16 Activities=1
contact sheets: 17 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
WARN: tab bar not reached
WARN: settings item =Диагностика|=Diagnostics not found
APP W/E lines (pids ['5156', '5925', '7820', '11986', '16904', '19691', '20167', '20641']): 817 in 224 groups
      34  W/IInputConnectionWrapper: endBatchEdit on inactive InputConnection
      30  W/IInputConnectionWrapper: getSelectedText on inactive InputConnection
      25  W/IInputConnectionWrapper: beginBatchEdit on inactive InputConnection
      24  W/IInputConnectionWrapper: getTextBeforeCursor on inactive InputConnection
      24  W/IInputConnectionWrapper: getTextAfterCursor on inactive InputConnection
      19  W/GooglePlayServicesUtil: Google Play services out of date for org.chyavorec.app.  Requires # but found #
      19  W/GoogleApiManager: The service for lg# is not available: lr#{statusCode=SERVICE_VERSION_UPDATE_REQUIRED, resolution=null, message
      18  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
      16  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
      16  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      15  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
      15  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      14  E/ResourceType: Style contains key with bad entry: #
      13  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
      13  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_MENU, scanCode=#, metaSt
      12  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
      11  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
      11  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
      10  E/eglCodecCommon: glUtilsParamSize: unknow param #
      10  W/ConnectionStatusConfig: Dynamic lookup for intent failed for action: com.google.android.gms.chimera.container.moduleinstall.ModuleInst
      10  W/GmsClient: unable to connect to service: com.google.android.gms.chimera.container.moduleinstall.ModuleInstallService.STAR
       9  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_MENU, scanCode=#, metaSt
       9  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
       8  W/zygote64: Unexpected CPU variant for X# using defaults: x#_#
       8  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       8  W/BasicSecureTextField: 	at o#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       5  E/AndroidRuntime: 	at m#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  E/AndroidRuntime: 	at o#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
contact sheets: 17 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5156', '5925', '7820', '11986', '16904', '19691', '20167', '20641']
== FATAL: 4
== ANR: 0
== StrictMode: 0
== Skipped: 18
   skipped-frames events=18 max=62 total=742
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 2 block(s) {'FATAL': 2} → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 83123049 Nov 12 20:46 app-mapping.txt; crashes: 19584 B
```

## c28

```
part c28; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2223 ms
## C1. Tour of all screens on API 28
## T-api28-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api28-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api28-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Последни новини · Празника „38 читалища. Един  · 27 септември 2026 · Сканирай · Читателска карта · Искам да прочета
CAP api28-light/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | 16 · НОЕ · Организацията на ООН за обра · БИБЛИОТЕКА · 21 · НОЕ
CAP api28-light/01d-home-4: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | За теб · Пътят на пътя · Огънят на здрача · Дъхът на любовта · ВИТРИНА · Най търсени
CAP api28-light/02-catalog: targets=22 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-light/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP api28-light/02c-filters-2: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=0 | Манипулатор за преместване с · продължаващо издание · патент/стандарт · нотно издание · Раздел (жанр) · Общ отдел
CAP api28-light/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api28-light/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-light/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-light/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=44 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api28-light/03-book: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api28-light/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api28-light/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api28-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Сърце — Бешлийс · Още една · Към книгата · Затвори
CAP api28-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CAP api28-light/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP api28-light/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api28-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api28-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api28-light/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api28-light/08b-news-2: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api28-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api28-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api28-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api28-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api28-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api28-light/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Панайот Пипков · Ден на семе · 21 ноември 2026 · Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана
CAP api28-light/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Панайот Пипков · Ден на семе · 21 ноември 2026 · Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана
CAP api28-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api28-light/12-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api28-light/12b-more-2: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия · Снимки от сайта
CAP api28-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api28-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api28-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · Сподели · Отвори в сайта · За снимката
CAP api28-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · 70-те години · Сподели · Отвори в сайта
CAP api28-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api28-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP api28-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api28-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api28-light/18-settings: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP api28-light/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP api28-light/18c-settings-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.35
CAP api28-light/18d-settings-4: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Корици по ISBN от covers.ope · Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни · Новините, събитията, каталог · Поверителност
CAP api28-light/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api28-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api28-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api28-light/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api28-light/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api28-light/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api28-light/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api28-light/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 2
CAP api28-light/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 2
CAP api28-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP api28-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api28-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api28-light: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=3 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=49
app theme → =Тъмна|=Dark: text="Изглед" text="Тема" text="Тъмна" text="Размер на текста" 
## T-api28-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api28-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api28-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Последни новини · Празника „38 читалища. Един  · 27 септември 2026 · Моите книги · Читателска карта · Искам да прочета
CAP api28-dark/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра · БИБЛИОТЕКА · 21
CAP api28-dark/01d-home-4: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | За теб · Пътят на пътя · Огънят на здрача · Дъхът на любовта · ВИТРИНА · Най търсени
CAP api28-dark/01e-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP api28-dark/02-catalog: targets=22 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-dark/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP api28-dark/02c-filters-2: targets=23 MISSING=2 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=3 | front: app | texts=27 ellipsized=0 | Манипулатор за преместване с · Раздел (жанр) · Общ отдел · Философия. Психология · Религия · Обществени науки
CAP api28-dark/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api28-dark/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-dark/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-dark/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=44 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api28-dark/03-book: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api28-dark/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api28-dark/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api28-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Златният горите · Още една · Към книгата · Затвори
CAP api28-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP api28-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP api28-dark/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api28-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api28-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api28-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api28-dark/08b-news-2: targets=21 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api28-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api28-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api28-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api28-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api28-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api28-dark/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Панайот Пипков · Ден на семе · 21 ноември 2026 · Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана
CAP api28-dark/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Панайот Пипков · Ден на семе · 21 ноември 2026 · Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана
CAP api28-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api28-dark/12-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api28-dark/12b-more-2: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия · Снимки от сайта
CAP api28-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api28-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api28-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · Сподели · Отвори в сайта · За снимката
CAP api28-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · 70-те години · Сподели · Отвори в сайта
CAP api28-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api28-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP api28-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api28-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api28-dark/18-settings: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Тъмна · Размер на текста · Стандартен · Палитра
CAP api28-dark/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP api28-dark/18c-settings-3: targets=7 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Обновления · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.35 · Данни
CAP api28-dark/18d-settings-4: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни · Новините, събитията, каталог · Поверителност · Искай отключване за личните 
CAP api28-dark/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api28-dark/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api28-dark/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api28-dark/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api28-dark/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api28-dark/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api28-dark/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api28-dark/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 3
CAP api28-dark/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 3
CAP api28-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP api28-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api28-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api28-dark: A11Y AGG: 57 screens analysed, 0 dumps failed; totals MISSING=7 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=51
WARN: Settings not found
A11Y AGG: 113 screens analysed, 0 dumps failed; totals MISSING=10 DOUBLE=0 SAME-LABEL=10 SMALL(<48dp)=100
== MISSING: 7 distinct
   View@[0,1906][1080,2032]  [1 screens: 02-catalog]
   View@[53,127][529,227]  [1 screens: 02c-filters-2]
   View@[550,127][929,227]  [1 screens: 02c-filters-2]
   View@[0,1932][1080,2058]  [1 screens: 08b-news-2]
   View@[42,2256][524,2274]  [1 screens: 13-gallery]
   View@[556,2256][1038,2274]  [1 screens: 13-gallery]
   View@[0,231][1080,243]  [1 screens: 18c-settings-3]
== SMALL: 23 distinct
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   ''  [2 screens: 02c-filters-2, 13-gallery]
   'Бевълстоук : Кн. 1 - 2, Куин, '  [2 screens: 02e-shelf, 02f-shelf-2]
   'Безсрамна добродетел, Робъртс,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият дома, Николай Симеонов,'  [2 screens: 02e-shelf, 02f-shelf-2]
   '12 -те вятъра, Гарфилд, Джудит'  [1 screens: 02e-shelf]
   'Аз обичам Ню Йорк, Келк, Линдз'  [1 screens: 02e-shelf]
   'Ако върнеш времето назад, Потъ'  [1 screens: 02e-shelf]
   'Алтъна, Никова, Ивелина'  [1 screens: 02e-shelf]
   'Анатомът, Андахази, Федерико'  [1 screens: 02e-shelf]
   'Белият звездите, Диана Христов'  [1 screens: 02e-shelf]
   'Всичко'  [1 screens: 02f-shelf-2]
   'Заглавие'  [1 screens: 02f-shelf-2]
   'Автор'  [1 screens: 02f-shelf-2]
   'ISBN'  [1 screens: 02f-shelf-2]
   'Белият вятъра, Петър Здравков'  [1 screens: 02f-shelf-2]
   'Белият детството, Симеон Колев'  [1 screens: 02f-shelf-2]
   'Запази: Фолклорно пътешествие '  [1 screens: 08-news]
   'Всички'  [1 screens: 08b-news-2]
   'Запазени'  [1 screens: 08b-news-2]
## C2. Home ribbon and the card on API 28
IDLE card28-3-18s (15 s, no input): frames +0; cpu=3.8% (ticks 57 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=3.1%, DefaultExecutor=0.7%, Jit thread pool=0.1%; pid 16707
CARD fullscreen on API 28: yes; app in front yes
IDLE cardfull28-30-45s (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 16707
## C3. Surprise ×2, share a book as an image on API 28
SURPRISE 1: text="Още една" text="Към книгата" ; app in front yes
SURPRISE 2: text="Още една" text="Към книгата" ; app in front yes
SHARE book28: chooser OPEN after 1 s; cache/share: 290328 2026-11-12 19:51 chyavorec-book-399560-1794513119426.png;
SHARE book28 file: chyavorec-book-399560-1794513119426.png 290328 B; PNG (1080, 1920) RGBA
SHARE book28 received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794513119426.png mime=image/png bytes=290328 png=true dims=1080x1920 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
## C5. Rotation and process death on API 28 (book, catalogue, article)
ROT book: pid 16707→16707 (same); landscape: before: no texts; back to portrait: before: no texts
PROCDEATH book: killed yes; new pid 17699; restored: before: no texts; front app
ROT catalog: pid 17699→17699 (same); landscape: kept 63% of 35 texts; lost e.g. ['·', 'налична', 'Белият дома', 'Белият пътя']; back to portrait: kept 91% of 35 texts; lost e.g. ['ISBN', 'Автор', 'Ключова дума']
PROCDEATH catalog: killed yes; new pid 18099; restored: kept 100% of 35 texts; front app
ROT article: pid 18099→18099 (same); landscape: kept 78% of 9 texts; lost e.g. ['СЪБИТИЕ  ·  25 юли 2026', 'Групата за народни танци при Н']; back to portrait: kept 100% of 9 texts
PROCDEATH article: killed yes; new pid 18355; restored: kept 100% of 9 texts; front app
## C6. Monkey 2000 (seed 1357) on API 28
## W. First launch and warm-up
first-install catalogue results after 1885 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=18665
MONKEY exit=0 duration=80 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '18665'; airplane=0; camera: 
MEM after-monkey (raw) PSS=77725KB  Java=16116KB  Native=36632KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=?KB  Views=19 Activities=1
MEM after-monkey (trimmed) PSS=67127KB  Java=7348KB  Native=34808KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=?KB  Views=15 Activities=1
contact sheets: 17 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 8 | Първи кадър: медиана 1197 мс, последен 1134 мс | Пълно показване: медиана 1971 мс, последно 1842 мс | Топъл старт · 2 | Първи кадър: медиана 247 мс, последен 92 мс | Пълно показване: медиана 403 мс, последно 403 мс | Насечени кадри по екран
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 8" text="Първи кадър: медиана 1197 мс, последен 1134 мс" text="Пълно показване: медиана 1971 мс, последно 1842 мс" text="Топъл старт · 2" text="Първи кадър: медиана 247 мс, последен 92 мс" text="Пълно показва
APP W/E lines (pids ['5457', '6288', '11584', '16707', '17699', '18099', '18355', '18665']): 455 in 105 groups
      11  W/IInputConnectionWrapper: beginBatchEdit on inactive InputConnection
      11  W/IInputConnectionWrapper: getTextBeforeCursor on inactive InputConnection
      11  W/IInputConnectionWrapper: getTextAfterCursor on inactive InputConnection
      11  W/IInputConnectionWrapper: getSelectedText on inactive InputConnection
      11  W/IInputConnectionWrapper: endBatchEdit on inactive InputConnection
       9  W/BasicSecureTextField: 	at o#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/g.chyavorec.ap: Unexpected CPU variant for X# using defaults: x#_#
       8  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       8  E/eglCodecCommon: glUtilsParamSize: unknow param #
       8  E/eglCodecCommon: GoldfishAddressSpaceHostMemoryAllocator: ioctl_ping failed for device_type=#, ret=-#
       8  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       8  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
       5  W/BasicSecureTextField: 	at ci#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       5  W/BasicSecureTextField: 	at o#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       5  W/BasicSecureTextField: 	at cs#.c(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       5  W/BasicSecureTextField: 	at p#.G#(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       5  W/BasicSecureTextField: 	at p#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       5  W/BasicSecureTextField: 	at xx.c(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       5  W/BasicSecureTextField: 	at rr#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       5  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
       5  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
       5  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
       5  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
       4  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       4  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       4  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       4  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       4  W/BasicSecureTextField: 	at h#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at vy#.b(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
contact sheets: 18 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5457', '6288', '11584', '16707', '17699', '18099', '18355', '18665']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=50 total=243
== Davey: 6
   davey events=6 max=1031ms median=858ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## c36

```
part c36; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4314 ms
## C1. Tour of all screens on API 36
## T-api36-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP api36-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api36-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · С хоро и песен във Водица · 26 септември 2026 · Сканирай · Читателска карта · Искам да прочета
CAP api36-light/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра · БИБЛИОТЕКА · 21
CAP api36-light/01d-home-4: targets=16 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя · Огънят на здрача · Дъхът на любовта · ВИТРИНА
CAP api36-light/01e-search-12: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=36 ellipsized=1 [Търси книга, новина, събитие…] | Лента за търсене · Търси книга, новина, събитие · Сканирай баркод · 12 ноември 2026 · Св. Йоан М · Последни новини · БИБЛИОТЕКА
CAP api36-light/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-light/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP api36-light/02c-filters-2: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=0 | Манипулатор за преместване с · патент/стандарт · нотно издание · Раздел (жанр) · Общ отдел · Философия. Психология
CAP api36-light/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api36-light/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-light/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-light/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api36-light/03-book: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api36-light/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api36-light/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api36-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Завръщането на  · Още една · Към книгата · Затвори
CAP api36-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CAP api36-light/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP api36-light/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api36-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api36-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api36-light/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api36-light/08b-news-2: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=4 | front: app | texts=28 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api36-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api36-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api36-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api36-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api36-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api36-light/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP api36-light/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP api36-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api36-light/12-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api36-light/12b-more-2: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=22 ellipsized=0 | Съобщения и покани от читали · Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия
CAP api36-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api36-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api36-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP api36-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP api36-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api36-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP api36-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api36-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api36-light/18-settings: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP api36-light/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български · Сезонни украси
CAP api36-light/18c-settings-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Известия · Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два 
CAP api36-light/18d-settings-4: targets=8 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Корици от интернет · Корици по ISBN от covers.ope · Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни · Новините, събитията, каталог
CAP api36-light/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api36-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api36-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api36-light/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api36-light/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api36-light/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api36-light/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api36-light/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 2
CAP api36-light/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 2
CAP api36-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP api36-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api36-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api36-light: A11Y AGG: 57 screens analysed, 0 dumps failed; totals MISSING=7 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=49
## T-api36-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night yes, app locale [bg-BG], rotation 0)
CAP api36-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api36-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Моите книги · Читателска карта
CAP api36-dark/01c-home-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | КАЛЕНДАР · Предстоящи събития · Всички · 16 · НОЕ · 80 г. от създаването на ЮНЕС
CAP api36-dark/01d-home-4: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=21 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP api36-dark/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-dark/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP api36-dark/02c-filters-2: targets=24 MISSING=1 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=0 | Манипулатор за преместване с · продължаващо издание · патент/стандарт · нотно издание · Раздел (жанр) · Общ отдел
CAP api36-dark/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api36-dark/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-dark/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-dark/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api36-dark/03-book: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api36-dark/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api36-dark/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api36-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Пътят на селото · Още една · Към книгата · Затвори
CAP api36-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP api36-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP api36-dark/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api36-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api36-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api36-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api36-dark/08b-news-2: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api36-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api36-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api36-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api36-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api36-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api36-dark/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP api36-dark/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP api36-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api36-dark/12-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api36-dark/12b-more-2: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=22 ellipsized=0 | Съобщения и покани от читали · Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия
CAP api36-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api36-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api36-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP api36-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP api36-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api36-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP api36-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api36-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api36-dark/18-settings: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP api36-dark/18b-settings-2: targets=9 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP api36-dark/18c-settings-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Разклати телефона в каталога · Известия · Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване
CAP api36-dark/18d-settings-4: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Данни · Корици от интернет · Корици по ISBN от covers.ope · Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни
CAP api36-dark/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api36-dark/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api36-dark/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api36-dark/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api36-dark/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api36-dark/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api36-dark/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api36-dark/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 3
CAP api36-dark/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 3
CAP api36-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP api36-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api36-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api36-dark: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=5 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=47
A11Y AGG: 113 screens analysed, 0 dumps failed; totals MISSING=12 DOUBLE=0 SAME-LABEL=10 SMALL(<48dp)=96
== MISSING: 9 distinct
   View@[53,2318][1027,2400]  [1 screens: 02c-filters-2]
   View@[556,296][1038,349]  [1 screens: 12b-more-2]
   View@[42,2321][524,2400]  [1 screens: 13-gallery]
   View@[556,2321][1038,2400]  [1 screens: 13-gallery]
   View@[0,2311][1080,2400]  [1 screens: 18b-settings-2]
   View@[42,0][378,64]  [1 screens: 01d-home-4]
   View@[415,0][751,64]  [1 screens: 01d-home-4]
   View@[788,0][1080,64]  [1 screens: 01d-home-4]
   View@[0,2305][1080,2400]  [1 screens: 18d-settings-4]
== SMALL: 24 distinct
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   'Всички'  [2 screens: 01d-home-4, 08b-news-2]
   'Бевълстоук : Кн. 1 - 2, Куин, '  [2 screens: 02e-shelf, 02f-shelf-2]
   'Безсрамна добродетел, Робъртс,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият дома, Николай Симеонов,'  [2 screens: 02e-shelf, 02f-shelf-2]
   '12 -те вятъра, Гарфилд, Джудит'  [1 screens: 02e-shelf]
   'Аз обичам Ню Йорк, Келк, Линдз'  [1 screens: 02e-shelf]
   'Ако върнеш времето назад, Потъ'  [1 screens: 02e-shelf]
   'Алтъна, Никова, Ивелина'  [1 screens: 02e-shelf]
   'Анатомът, Андахази, Федерико'  [1 screens: 02e-shelf]
   'Белият звездите, Диана Христов'  [1 screens: 02e-shelf]
   'Всичко'  [1 screens: 02f-shelf-2]
   'Заглавие'  [1 screens: 02f-shelf-2]
   'Автор'  [1 screens: 02f-shelf-2]
   'ISBN'  [1 screens: 02f-shelf-2]
   'Белият вятъра, Петър Здравков'  [1 screens: 02f-shelf-2]
   'Белият детството, Симеон Колев'  [1 screens: 02f-shelf-2]
   'Запазени'  [1 screens: 08b-news-2]
## C2. Home ribbon and the card on API 36
IDLE card36-3-18s (15 s, no input): frames +121; cpu=51.9% (ticks 778 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=50.8%, g.chyavorec.app=0.7%, Jit thread pool=0.3%, DefaultExecutor=0.1%; pid 15314
CARD fullscreen on API 36: yes; app in front yes
IDLE cardfull36-30-45s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 15314
## C3. Surprise ×2, share a book as an image on API 36
SURPRISE 1: text="Още една" text="Към книгата" ; app in front yes
SURPRISE 2: text="Още една" text="Към книгата" ; app in front yes
SHARE book36: chooser OPEN after 3 s; cache/share: 774373 2026-11-12 19:55 chyavorec-book-399560-1794513348021.png;
SHARE book36 file: chyavorec-book-399560-1794513348021.png 774373 B; PNG (1080, 1920) RGBA
SHARE book36 received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794513348021.png mime=image/png bytes=774373 png=true dims=1080x1920 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
## C4. Predictive back (gesture navigation) on API 36: „Изненадай ме“, a book, an article
gesture nav:  navigation_mode=2
enable_back_animation: null
PBACK surprise open: text="Още една" text="Към книгата" 
PBACK surprise after a cancelled gesture: still open (expected)
PBACK surprise after the committed gesture: closed; catalogue shown; in front app
PBACK surprise mid-gesture vs open: diff 2.37
PBACK book: after the gesture book screen STILL OPEN; in front app
PBACK article: closed; in front app
PBACK viewer: closed; in front app
PBACK on Home (root): in front after the gesture: com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity
## C5. Rotation and process death on API 36 (book, catalogue, article)
ROT book: pid 15314→15314 (same); landscape: kept 38% of 24 texts; lost e.g. ['Вид', 'Език', 'книга', 'Отдел']; back to portrait: kept 100% of 24 texts
PROCDEATH book: killed yes; new pid 17643; restored: kept 100% of 24 texts; front app
ROT catalog: pid 17643→17643 (same); landscape: kept 71% of 34 texts; lost e.g. ['Белият пътя', 'Белият дома', 'Мартин Тонев', 'Ключова дума']; back to portrait: kept 100% of 34 texts
PROCDEATH catalog: killed yes; new pid 18075; restored: kept 100% of 34 texts; front app
ROT article: pid 18075→18075 (same); landscape: kept 78% of 9 texts; lost e.g. ['СЪБИТИЕ  ·  25 юли 2026', 'Групата за народни танци при Н']; back to portrait: kept 100% of 9 texts
PROCDEATH article: killed yes; new pid 18377; restored: kept 100% of 9 texts; front app
## C6. Monkey 2000 (seed 1357) on API 36
## W. First launch and warm-up
first-install catalogue results after 2411 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=18722
MONKEY exit=0 duration=129 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '18722'; airplane=0; camera: 
MEM after-monkey (raw) PSS=87424KB  Java=28288KB  Native=32596KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=26)  RSS=206932KB  Views=30 Activities=1
MEM after-monkey (trimmed) PSS=75904KB  Java=14992KB  Native=29452KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=26)  RSS=195992KB  Views=17 Activities=1
contact sheets: 18 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 8 | Първи кадър: медиана 1587 мс, последен 1802 мс | Пълно показване: медиана 3256 мс, последно 3255 мс | Топъл старт · 5 | Първи кадър: медиана 1147 мс, последен 938 мс | Пълно показване: медиана 1421 мс, последно 1337 мс | Насечени кадри по е
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 8" text="Първи кадър: медиана 1587 мс, последен 1802 мс" text="Пълно показване: медиана 3256 мс, последно 3255 мс" text="Топъл старт · 5" text="Първи кадър: медиана 1147 мс, последен 938 мс" text="Пълно показ
APP W/E lines (pids ['4412', '5215', '10707', '15314', '17643', '18075', '18377', '18722']): 1990 in 160 groups
     508  W/Codec2Client: query -- param skipped: index = #.
     353  W/ColorUtils: expected specified color aspects (#:#:#:#)
     184  W/HWUI: Image decoding logging dropped!
     127  E/MediaCodec: Media Quality Service not found.
     127  W/CCodec: This behavior is subject to change. It is recommended that app developers double check whether the requested m
     127  E/g.chyavorec.app: Failed to query component interface for required system resources: #
      38  W/FrameTracker: Missed SF frame:JANK_COMPOSER, #, #, CUJ=J<IME_INSETS_SHOW_ANIMATION::#@#@org.chyavorec.app>
      33  W/FrameTracker: Missed SF frame:JANK_COMPOSER, #, #, CUJ=J<IME_INSETS_HIDE_ANIMATION::#@#@org.chyavorec.app>
      16  W/RemoteInputConnectionImpl: requestCursorUpdates on inactive InputConnection
      13  W/HWUI: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      13  W/HWUI: Failed to initialize #-# format, error = EGL_SUCCESS
      10  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=android.view.ImeBackAnimationController@#ece#
       9  W/AMessage: failed to deliver message as target handler # is gone.
       9  W/FrameTracker: Missed App frame:UNKNOWN: #, #, #, CUJ=J<IME_INSETS_SHOW_ANIMATION::#@#@org.chyavorec.app>
       9  W/FrameTracker: Missed SF frame:UNKNOWN: #, #, #, CUJ=J<IME_INSETS_SHOW_ANIMATION::#@#@org.chyavorec.app>
       8  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       8  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, goldmont, goldmont-plus, goldmont-without-sha-xsaves, tremont, 
       8  E/g.chyavorec.app: Not starting debugger since process cannot load the jdwp agent.
       8  E/ashmem: Pinning is deprecated since Android Q. Please use trim or other methods.
       8  W/HWUI: Unknown dataspace #
       8  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       7  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       6  W/BasicSecureTextField: 	at o#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       6  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=android.view.ImeBackAnimationController@#cb#f#d
       5  E/ImeBackDispatcher: Ime callback not found. Ignoring unregisterReceivedCallback. callbackId: #
       5  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=android.view.ImeBackAnimationController@#f#
       4  W/InteractionJankMonitor: Initializing without READ_DEVICE_CONFIG permission. enabled=true, interval=#, missedFrameThreshold=#, frameTim
       4  W/g.chyavorec.app: Cleared Reference was only reachable from finalizer (only reported once)
       4  W/FrameTracker: Missed App frame:UNKNOWN: #, #, #, CUJ=J<IME_INSETS_HIDE_ANIMATION::#@#@org.chyavorec.app>
contact sheets: 19 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4412', '5215', '10707', '15314', '17643', '18075', '18377', '18722']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 74
   skipped-frames events=74 max=82 total=3295
== Davey: 192
   davey events=192 max=1918ms median=899ms
== NativeLink: 7
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## crash26

```
part crash26; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 8.0.0 sdk 26; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-v26
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2038 ms
## CR1. Monkey 1500 events, seed 777, plain (the crash of round 2: seed 777, after 148 events)
CR1 seed 777 plain: Events injected: 53  CRASH=1; FATAL in logcat since the start: 0
## CR2. Monkey 1500 events, seed 777, rec (the crash of round 2: seed 777, after 148 events)
WARN: tab bar not reached
CR2 seed 777 rec: Events injected: 1500  CRASH=0; FATAL in logcat since the start: 0
## CR3. Monkey 1500 events, seed 1357, rec (the crash of round 2: seed 777, after 148 events)
CR3 seed 1357 rec: Events injected: 1500  CRASH=0; FATAL in logcat since the start: 0
## CR4. Monkey 1500 events, seed 4242, rec (the crash of round 2: seed 777, after 148 events)
CR4 seed 4242 rec: Events injected: 1500  CRASH=0; FATAL in logcat since the start: 0
## CR5. Monkey 1500 events, seed 31337, rec (the crash of round 2: seed 777, after 148 events)
CR5 seed 31337 rec: Events injected: 1500  CRASH=0; FATAL in logcat since the start: 0
## CR6. Monkey 1500 events, seed 99, rec (the crash of round 2: seed 777, after 148 events)
CR6 seed 99 rec: Events injected: 1500  CRASH=0; FATAL in logcat since the start: 0
## CR7. Monkey 1500 events, seed 2024, rec (the crash of round 2: seed 777, after 148 events)
CR7 seed 2024 rec: Events injected: 1500  CRASH=0; FATAL in logcat since the start: 0
## CR8. Monkey 1500 events, seed 5, rec (the crash of round 2: seed 777, after 148 events)
CR8 seed 5 rec: Events injected: 1500  CRASH=0; FATAL in logcat since the start: 0
## CR9. Monkey 1500 events, seed 777, rec (the crash of round 2: seed 777, after 148 events)
CR9 seed 777 rec: Events injected: 1500  CRASH=0; FATAL in logcat since the start: 0
contact sheets: 0 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 11 | Първи кадър: медиана 1238 мс, последен 1286 мс | Пълно показване: медиана 1778 мс, последно 1703 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 11" text="Първи кадър: медиана 1238 мс, последен 1286 мс" text="Пълно показване: медиана 1778 мс, последно 1703 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['5003', '5918', '6210', '7169', '8053', '8972', '9909', '10919', '11785', '12498', '13208']): 1024 in 44 groups
     136  W/IInputConnectionWrapper: endBatchEdit on inactive InputConnection
     121  W/IInputConnectionWrapper: getSelectedText on inactive InputConnection
     120  W/IInputConnectionWrapper: beginBatchEdit on inactive InputConnection
     118  W/IInputConnectionWrapper: getTextAfterCursor on inactive InputConnection
     112  W/IInputConnectionWrapper: getTextBeforeCursor on inactive InputConnection
      42  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
      34  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
      30  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
      29  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
      26  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
      24  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      23  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
      22  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
      20  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_MENU, scanCode=#, metaSt
      19  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_MENU, scanCode=#, metaSt
      18  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
      17  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      14  E/eglCodecCommon: glUtilsParamSize: unknow param #
      11  W/zygote64: Unexpected CPU variant for X# using defaults: x#_#
      11  E/ResourceType: Style contains key with bad entry: #
      11  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       9  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=#.#, y[
       8  W/InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
       6  W/ViewRootImpl[MainActivity]: Dropping event due to root view being removed: MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]
       6  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=-#.#, y
       6  W/IInputConnectionWrapper: setComposingText on inactive InputConnection
       3  W/InputMethodManager: Ignoring onBind: cur seq=#, given seq=#
       3  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: MotionEvent { action=ACTION_DOWN, actionButton=#, id[#]=#, x[#]=#.#, y[
       3  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: MotionEvent { action=ACTION_CANCEL, actionButton=#, id[#]=#, x[#]=#.#
contact sheets: 1 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5003', '5918', '6210', '7169', '8053', '8972', '9909', '10919', '11785', '12498', '13208']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 32
   skipped-frames events=32 max=58 total=1240
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 2 block(s) {'SIGNAL': 2} → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 83123049 Nov 12 21:43 app-mapping.txt; crashes: 231 B
```

## gfx35

```
part gfx35; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## P0. Package: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest); dexopt: x86_64: [status=verify] x86_64: [status=verify] ; baseline profile in the APK: assets/dexopt/baseline.prof assets/dexopt/baseline.profm 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 6512 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## P1. 10 cold (with 'Fully drawn') + 10 warm starts, as installed
START cold new           n=10 min=3129ms median=3602ms max=4432ms  all=[4137, 3619, 3129, 3636, 4432, 3779, 3584, 3321, 3350, 3146]  states=['COLD']
DRAWN new: TotalTime n=10 min=3129ms median=3602ms max=4432ms; fully drawn n=10 median=4478ms min=3587 max=5859 all=[5590, 4377, 4578, 5576, 5859, 5002, 4227, 3753, 4303, 3587]; without line: 0
START warm new           n=9 min=576ms median=873ms max=1108ms  all=[1108, 690, 905, 873, 991, 601, 971, 576, 648]  states=['HOT', 'UNKNOWN']
## G1. gfxinfo per scenario, round 1 (animations ON, 1080x2400); slowest frames from framestats and the histogram
GFX r1-home-scroll frames=   63  janky=63 (100.00%)  p50=400ms p90=550ms p95=650ms p99=800ms  slowUI=43 slowDraw=63 highInput=0
  GFXMEM r1-home-scroll: pipeline: Skia (OpenGL); GPU total: 39517440 bytes, 37.69 MB (32.13 MB is purgeable); textures: 3.27 MB (7 entries) | 6.17 MB (34 entries) | 8.66 MB (29 entries); frames: 63
  FS r1-home-scroll: n=63 slowest=879.3ms (frame #4; ui 203.2 rt 349.2 issue 137.3 flags 0) first5=[267, 629, 864, 879, 818] >32ms=63 >64ms=63 >100ms=63 span=13033ms
  r1-home-scroll HIST: frames=63 slowest-bucket=800ms ≥50ms=63 ≥100ms=63 ≥200ms=58 ≥500ms=20
GFX r1-carousel  frames=   27  janky=27 (100.00%)  p50=350ms p90=550ms p95=550ms p99=650ms  slowUI=21 slowDraw=27 highInput=0
  GFXMEM r1-carousel: pipeline: Skia (OpenGL); GPU total: 42485452 bytes, 40.52 MB (35.54 MB is purgeable); textures: 2.32 MB (5 entries) | 8.76 MB (31 entries) | 9.87 MB (37 entries); frames: 27
  FS r1-carousel: n=27 slowest=743.8ms (frame #21; ui 484.3 rt 194.3 issue 147.1 flags 0) first5=[99, 264, 357, 526, 530] >32ms=27 >64ms=27 >100ms=26 span=4733ms
  r1-carousel HIST: frames=27 slowest-bucket=650ms ≥50ms=27 ≥100ms=26 ≥200ms=24 ≥500ms=6
GFX r1-news      frames=   52  janky=52 (100.00%)  p50=300ms p90=350ms p95=400ms p99=450ms  slowUI=27 slowDraw=52 highInput=0
  GFXMEM r1-news: pipeline: Skia (OpenGL); GPU total: 78200056 bytes, 74.58 MB (69.56 MB is purgeable); textures: 2.32 MB (5 entries) | 11.91 MB (75 entries) | 9.63 MB (36 entries); frames: 52
  FS r1-news: n=52 slowest=591.1ms (frame #44; ui 268.4 rt 199.3 issue 99.7 flags 0) first5=[77, 123, 229, 431, 403] >32ms=52 >64ms=52 >100ms=51 span=7483ms
  r1-news HIST: frames=52 slowest-bucket=450ms ≥50ms=52 ≥100ms=50 ≥200ms=44 ≥500ms=0
GFX r1-article   frames=   73  janky=71 (97.26%)  p50=250ms p90=400ms p95=450ms p99=450ms  slowUI=32 slowDraw=71 highInput=2
  GFXMEM r1-article: pipeline: Skia (OpenGL); GPU total: 89166224 bytes, 85.04 MB (81.97 MB is purgeable); textures: 2.33 MB (6 entries) | 12.27 MB (80 entries) | 7.04 MB (23 entries); frames: 73
  FS r1-article: n=73 slowest=630.4ms (frame #67; ui 217.0 rt 245.3 issue 65.2 flags 0) first5=[55, 251, 338, 584, 592] >32ms=73 >64ms=72 >100ms=72 span=12267ms
  r1-article HIST: frames=73 slowest-bucket=450ms ≥50ms=73 ≥100ms=72 ≥200ms=60 ≥500ms=0
GFX r1-calendar  frames=   44  janky=43 (97.73%)  p50=200ms p90=350ms p95=400ms p99=450ms  slowUI=28 slowDraw=43 highInput=1
  GFXMEM r1-calendar: pipeline: Skia (OpenGL); GPU total: 88389112 bytes, 84.29 MB (81.97 MB is purgeable); textures: 13.85 KB (1 entry) | 10.73 MB (49 entries) | 6.68 MB (44 entries); frames: 44
  FS r1-calendar: n=44 slowest=617.5ms (frame #29; ui 179.7 rt 247.9 issue 65.7 flags 0) first5=[62, 100, 266, 308, 228] >32ms=44 >64ms=41 >100ms=38 span=6783ms
  r1-calendar HIST: frames=44 slowest-bucket=450ms ≥50ms=44 ≥100ms=38 ≥200ms=26 ≥500ms=0
GFX r1-catalog   frames=   57  janky=57 (100.00%)  p50=400ms p90=600ms p95=600ms p99=750ms  slowUI=28 slowDraw=57 highInput=0
  GFXMEM r1-catalog: pipeline: Skia (OpenGL); GPU total: 88390816 bytes, 84.30 MB (55.03 MB is purgeable); textures: 346.24 KB (17 entries) | 2.08 MB (4 entries); frames: 57
  FS r1-catalog: n=57 slowest=862.2ms (frame #38; ui 271.7 rt 521.3 issue 310.1 flags 0) first5=[146, 367, 598, 824, 782] >32ms=57 >64ms=57 >100ms=57 span=13900ms
  r1-catalog HIST: frames=57 slowest-bucket=750ms ≥50ms=57 ≥100ms=57 ≥200ms=46 ≥500ms=11
GFX r1-shelf     frames=   64  janky=63 (98.44%)  p50=350ms p90=500ms p95=550ms p99=600ms  slowUI=31 slowDraw=63 highInput=1
  GFXMEM r1-shelf: pipeline: Skia (OpenGL); GPU total: 77638872 bytes, 74.04 MB (59.70 MB is purgeable); textures: 303.72 KB (20 entries) | 2.26 MB (5 entries); frames: 64
  FS r1-shelf: n=64 slowest=777.3ms (frame #5; ui 159.3 rt 456.2 issue 294.8 flags 0) first5=[120, 200, 328, 723, 777] >32ms=64 >64ms=64 >100ms=64 span=12067ms
  r1-shelf HIST: frames=64 slowest-bucket=600ms ≥50ms=64 ≥100ms=64 ≥200ms=55 ≥500ms=7
GFX r1-my        frames=   74  janky=74 (100.00%)  p50=300ms p90=400ms p95=450ms p99=500ms  slowUI=34 slowDraw=74 highInput=0
  GFXMEM r1-my: pipeline: Skia (OpenGL); GPU total: 61779992 bytes, 58.92 MB (56.57 MB is purgeable); textures: 608.82 KB (39 entries) | 2.25 MB (4 entries); frames: 74
  FS r1-my: n=74 slowest=683.5ms (frame #72; ui 262.5 rt 244.1 issue 60.0 flags 0) first5=[132, 293, 383, 556, 608] >32ms=74 >64ms=74 >100ms=74 span=11733ms
  r1-my HIST: frames=74 slowest-bucket=500ms ≥50ms=74 ≥100ms=74 ≥200ms=61 ≥500ms=1
GFX r1-more      frames=   79  janky=78 (98.73%)  p50=300ms p90=400ms p95=450ms p99=550ms  slowUI=38 slowDraw=78 highInput=1
  GFXMEM r1-more: pipeline: Skia (OpenGL); GPU total: 63391228 bytes, 60.45 MB (58.08 MB is purgeable); textures: 903.91 KB (56 entries) | 2.25 MB (3 entries); frames: 79
  FS r1-more: n=79 slowest=677.0ms (frame #31; ui 172.8 rt 426.4 issue 181.9 flags 0) first5=[59, 236, 354, 638, 648] >32ms=79 >64ms=78 >100ms=78 span=12300ms
  r1-more HIST: frames=79 slowest-bucket=550ms ≥50ms=79 ≥100ms=78 ≥200ms=63 ≥500ms=2
GFX r1-gallery   frames=   65  janky=64 (98.46%)  p50=200ms p90=400ms p95=450ms p99=500ms  slowUI=30 slowDraw=64 highInput=1
  GFXMEM r1-gallery: pipeline: Skia (OpenGL); GPU total: 78088264 bytes, 74.47 MB (71.96 MB is purgeable); textures: 5.36 MB (33 entries) | 2.84 MB (39 entries); frames: 65
  FS r1-gallery: n=65 slowest=639.3ms (frame #18; ui 172.7 rt 327.8 issue 124.3 flags 0) first5=[53, 89, 220, 352, 318] >32ms=65 >64ms=61 >100ms=57 span=11467ms
  r1-gallery HIST: frames=65 slowest-bucket=500ms ≥50ms=64 ≥100ms=57 ≥200ms=41 ≥500ms=1
GFX r1-album-grid frames=   73  janky=72 (98.63%)  p50=250ms p90=400ms p95=450ms p99=500ms  slowUI=35 slowDraw=72 highInput=2
  GFXMEM r1-album-grid: pipeline: Skia (OpenGL); GPU total: 86949672 bytes, 82.92 MB (80.35 MB is purgeable); textures: 6.75 MB (64 entries) | 3.18 MB (61 entries); frames: 73
  FS r1-album-grid: n=73 slowest=677.9ms (frame #50; ui 236.6 rt 263.7 issue 5.9 flags 0) first5=[55, 94, 283, 379, 330] >32ms=73 >64ms=70 >100ms=67 span=11383ms
  r1-album-grid HIST: frames=73 slowest-bucket=500ms ≥50ms=72 ≥100ms=67 ≥200ms=53 ≥500ms=3
GFX r1-viewer    frames=  161  janky=158 (98.14%)  p50=150ms p90=250ms p95=250ms p99=350ms  slowUI=81 slowDraw=158 highInput=4
  r1-viewer HIST: frames=161 slowest-bucket=350ms ≥50ms=150 ≥100ms=134 ≥200ms=63 ≥500ms=0
GFX r1-settings  frames=   71  janky=69 (97.18%)  p50=250ms p90=400ms p95=450ms p99=550ms  slowUI=24 slowDraw=69 highInput=3
  GFXMEM r1-settings: pipeline: Skia (OpenGL); GPU total: 62718288 bytes, 59.81 MB (57.40 MB is purgeable); textures: 5.69 MB (110 entries) | 3.62 MB (24 entries); frames: 71
  FS r1-settings: n=70 slowest=717.1ms (frame #61; ui 508.9 rt 83.3 issue 3.1 flags 1) first5=[118, 142, 31, 109, 126] >32ms=68 >64ms=67 >100ms=64 span=10750ms
  r1-settings HIST: frames=71 slowest-bucket=550ms ≥50ms=67 ≥100ms=62 ≥200ms=42 ≥500ms=3
GFX r1-tab-switch frames=   73  janky=72 (98.63%)  p50=200ms p90=500ms p95=600ms p99=950ms  slowUI=44 slowDraw=72 highInput=1
  r1-tab-switch HIST: frames=73 slowest-bucket=950ms ≥50ms=72 ≥100ms=66 ≥200ms=43 ≥500ms=9
GFX r1-surprise-x3 frames=   73  janky=73 (100.00%)  p50=400ms p90=750ms p95=800ms p99=1200ms  slowUI=46 slowDraw=73 highInput=0
  r1-surprise-x3 HIST: frames=73 slowest-bucket=1200ms ≥50ms=73 ≥100ms=68 ≥200ms=64 ≥500ms=25
GFX r1-cardfull-open-12s frames=   68  janky=65 (95.59%)  p50=200ms p90=350ms p95=350ms p99=900ms  slowUI=14 slowDraw=65 highInput=3
  FS r1-cardfull: n=69 slowest=906.8ms (frame #1; ui 629.6 rt 275.0 issue 160.3 flags 0) first5=[907, 521, 586, 598, 572] >32ms=69 >64ms=69 >100ms=69 span=13083ms
MEM after-round-1 (raw) PSS=98754KB  Java=23000KB  Native=45744KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=192820KB  Views=13 Activities=1
MEM after-round-1 (trimmed) PSS=86845KB  Java=10964KB  Native=41048KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=181348KB  Views=13 Activities=1
## G2. gfxinfo per scenario, round 2 (animations ON, 1080x2400); slowest frames from framestats and the histogram
GFX r2-home-scroll frames=   33  janky=33 (100.00%)  p50=350ms p90=550ms p95=600ms p99=600ms  slowUI=26 slowDraw=33 highInput=0
  GFXMEM r2-home-scroll: pipeline: Skia (OpenGL); GPU total: 29017808 bytes, 27.67 MB (22.95 MB is purgeable); textures: 2.83 MB (6 entries) | 7.27 MB (31 entries) | 7.84 MB (24 entries); frames: 33
  FS r2-home-scroll: n=32 slowest=784.4ms (frame #9; ui 268.7 rt 377.7 issue 139.9 flags 0) first5=[198, 513, 721, 757, 716] >32ms=32 >64ms=31 >100ms=31 span=7400ms
  r2-home-scroll HIST: frames=33 slowest-bucket=600ms ≥50ms=32 ≥100ms=32 ≥200ms=27 ≥500ms=9
GFX r2-carousel  frames=   11  janky=11 (100.00%)  p50=250ms p90=500ms p95=600ms p99=600ms  slowUI=5 slowDraw=11 highInput=0
  GFXMEM r2-carousel: pipeline: Skia (OpenGL); GPU total: 39614048 bytes, 37.78 MB (33.06 MB is purgeable); textures: 2.83 MB (6 entries) | 7.28 MB (35 entries) | 7.84 MB (24 entries); frames: 11
  FS r2-carousel: n=11 slowest=692.3ms (frame #11; ui 207.9 rt 308.3 issue 250.9 flags 0) first5=[76, 142, 356, 422, 678] >32ms=11 >64ms=11 >100ms=10 span=1350ms
  r2-carousel HIST: frames=11 slowest-bucket=600ms ≥50ms=11 ≥100ms=10 ≥200ms=8 ≥500ms=2
GFX r2-news      frames=   57  janky=57 (100.00%)  p50=300ms p90=350ms p95=400ms p99=400ms  slowUI=34 slowDraw=57 highInput=0
  GFXMEM r2-news: pipeline: Skia (OpenGL); GPU total: 77212216 bytes, 73.64 MB (68.86 MB is purgeable); textures: 10.40 MB (63 entries) | 10.92 MB (36 entries); frames: 57
  FS r2-news: n=57 slowest=580.0ms (frame #13; ui 185.8 rt 252.9 issue 72.7 flags 0) first5=[77, 130, 225, 327, 382] >32ms=57 >64ms=57 >100ms=56 span=8083ms
  r2-news HIST: frames=57 slowest-bucket=400ms ≥50ms=57 ≥100ms=55 ≥200ms=42 ≥500ms=0
GFX r2-article   frames=   73  janky=73 (100.00%)  p50=300ms p90=400ms p95=500ms p99=800ms  slowUI=41 slowDraw=73 highInput=0
  GFXMEM r2-article: pipeline: Skia (OpenGL); GPU total: 89074344 bytes, 84.95 MB (82.20 MB is purgeable); textures: 13.85 KB (1 entry) | 6.38 MB (71 entries) | 9.80 MB (31 entries); frames: 73
  FS r2-article: n=73 slowest=851.8ms (frame #72; ui 397.3 rt 245.0 issue 187.2 flags 0) first5=[55, 153, 374, 460, 516] >32ms=73 >64ms=72 >100ms=72 span=12450ms
  r2-article HIST: frames=73 slowest-bucket=800ms ≥50ms=73 ≥100ms=72 ≥200ms=59 ≥500ms=5
GFX r2-calendar  frames=   54  janky=54 (100.00%)  p50=250ms p90=400ms p95=450ms p99=450ms  slowUI=36 slowDraw=54 highInput=0
  GFXMEM r2-calendar: pipeline: Skia (OpenGL); GPU total: 88512184 bytes, 84.41 MB (82.16 MB is purgeable); textures: 2.04 MB (29 entries) | 4.37 MB (38 entries); frames: 54
  FS r2-calendar: n=54 slowest=628.9ms (frame #36; ui 198.0 rt 258.3 issue 141.2 flags 0) first5=[58, 102, 201, 230, 338] >32ms=54 >64ms=52 >100ms=52 span=7750ms
  r2-calendar HIST: frames=54 slowest-bucket=450ms ≥50ms=54 ≥100ms=52 ≥200ms=36 ≥500ms=0
GFX r2-catalog   frames=   63  janky=63 (100.00%)  p50=300ms p90=450ms p95=450ms p99=600ms  slowUI=30 slowDraw=63 highInput=0
  GFXMEM r2-catalog: pipeline: Skia (OpenGL); GPU total: 86401128 bytes, 82.40 MB (75.86 MB is purgeable); textures: 1.54 MB (22 entries) | 2.63 MB (42 entries); frames: 63
  FS r2-catalog: n=63 slowest=744.4ms (frame #15; ui 188.7 rt 302.0 issue 151.6 flags 0) first5=[87, 170, 324, 553, 533] >32ms=63 >64ms=63 >100ms=62 span=12133ms
  r2-catalog HIST: frames=63 slowest-bucket=600ms ≥50ms=63 ≥100ms=62 ≥200ms=53 ≥500ms=2
GFX r2-my        frames=   69  janky=68 (98.55%)  p50=300ms p90=400ms p95=450ms p99=500ms  slowUI=35 slowDraw=68 highInput=1
  GFXMEM r2-my: pipeline: Skia (OpenGL); GPU total: 88938616 bytes, 84.82 MB (78.26 MB is purgeable); textures: 616.58 KB (43 entries) | 2.33 MB (21 entries); frames: 69
  FS r2-my: n=69 slowest=639.1ms (frame #41; ui 211.9 rt 271.7 issue 153.3 flags 0) first5=[54, 147, 307, 468, 601] >32ms=69 >64ms=68 >100ms=68 span=12383ms
  r2-my HIST: frames=69 slowest-bucket=500ms ≥50ms=69 ≥100ms=68 ≥200ms=57 ≥500ms=2
GFX r2-more      frames=   77  janky=77 (100.00%)  p50=300ms p90=400ms p95=450ms p99=450ms  slowUI=35 slowDraw=77 highInput=0
  GFXMEM r2-more: pipeline: Skia (OpenGL); GPU total: 89181984 bytes, 85.05 MB (78.47 MB is purgeable); textures: 651.28 KB (54 entries) | 2.28 MB (7 entries); frames: 77
  FS r2-more: n=77 slowest=661.8ms (frame #44; ui 114.1 rt 252.8 issue 129.8 flags 0) first5=[58, 105, 265, 431, 523] >32ms=77 >64ms=76 >100ms=75 span=11967ms
  r2-more HIST: frames=77 slowest-bucket=450ms ≥50ms=77 ≥100ms=74 ≥200ms=60 ≥500ms=0
GFX r2-gallery   frames=   73  janky=72 (98.63%)  p50=250ms p90=350ms p95=400ms p99=500ms  slowUI=38 slowDraw=72 highInput=1
  GFXMEM r2-gallery: pipeline: Skia (OpenGL); GPU total: 82462480 bytes, 78.64 MB (71.97 MB is purgeable); textures: 5.06 MB (15 entries) | 2.89 MB (56 entries); frames: 73
  FS r2-gallery: n=73 slowest=643.7ms (frame #14; ui 126.2 rt 269.8 issue 140.5 flags 0) first5=[55, 95, 201, 368, 293] >32ms=73 >64ms=69 >100ms=67 span=11717ms
  r2-gallery HIST: frames=73 slowest-bucket=500ms ≥50ms=71 ≥100ms=66 ≥200ms=51 ≥500ms=2
GFX r2-album-grid frames=   74  janky=74 (100.00%)  p50=250ms p90=500ms p95=600ms p99=900ms  slowUI=51 slowDraw=74 highInput=0
  GFXMEM r2-album-grid: pipeline: Skia (OpenGL); GPU total: 83447576 bytes, 79.58 MB (77.19 MB is purgeable); textures: 6.76 MB (69 entries) | 2.81 MB (73 entries); frames: 74
  FS r2-album-grid: n=74 slowest=1085.9ms (frame #40; ui 571.0 rt 378.4 issue 162.8 flags 0) first5=[49, 168, 267, 362, 362] >32ms=74 >64ms=73 >100ms=72 span=13017ms
  r2-album-grid HIST: frames=74 slowest-bucket=900ms ≥50ms=73 ≥100ms=72 ≥200ms=55 ≥500ms=12
GFX r2-viewer    frames=  145  janky=144 (99.31%)  p50=150ms p90=250ms p95=300ms p99=350ms  slowUI=77 slowDraw=144 highInput=1
  r2-viewer HIST: frames=145 slowest-bucket=400ms ≥50ms=135 ≥100ms=122 ≥200ms=65 ≥500ms=0
GFX r2-settings  frames=   18  janky=17 (94.44%)  p50=200ms p90=350ms p95=400ms p99=400ms  slowUI=11 slowDraw=17 highInput=2
  GFXMEM r2-settings: pipeline: Skia (OpenGL); GPU total: 62837632 bytes, 59.93 MB (57.65 MB is purgeable); textures: 5.59 MB (90 entries) | 3.71 MB (40 entries); frames: 18
  FS r2-settings: n=17 slowest=499.7ms (frame #9; ui 101.3 rt 249.7 issue 138.8 flags 0) first5=[55, 141, 299, 296, 273] >32ms=17 >64ms=16 >100ms=16 span=1600ms
  r2-settings HIST: frames=18 slowest-bucket=400ms ≥50ms=17 ≥100ms=16 ≥200ms=11 ≥500ms=0
GFX r2-tab-switch frames=  113  janky=112 (99.12%)  p50=200ms p90=400ms p95=450ms p99=500ms  slowUI=63 slowDraw=112 highInput=1
  r2-tab-switch HIST: frames=113 slowest-bucket=500ms ≥50ms=109 ≥100ms=103 ≥200ms=70 ≥500ms=5
GFX r2-surprise-x3 frames=   68  janky=68 (100.00%)  p50=450ms p90=750ms p95=850ms p99=1200ms  slowUI=46 slowDraw=68 highInput=0
  r2-surprise-x3 HIST: frames=68 slowest-bucket=1200ms ≥50ms=68 ≥100ms=64 ≥200ms=54 ≥500ms=32
GFX r2-cardfull-open-12s frames=   68  janky=68 (100.00%)  p50=200ms p90=300ms p95=350ms p99=500ms  slowUI=15 slowDraw=68 highInput=0
  FS r2-cardfull: n=69 slowest=640.7ms (frame #5; ui 166.8 rt 255.7 issue 63.1 flags 0) first5=[139, 606, 245, 485, 641] >32ms=69 >64ms=69 >100ms=69 span=12383ms
MEM after-round-2 (raw) PSS=96955KB  Java=21800KB  Native=47320KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=5)  RSS=192872KB  Views=13 Activities=1
MEM after-round-2 (trimmed) PSS=85074KB  Java=10404KB  Native=42004KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=5)  RSS=181636KB  Views=13 Activities=1
## P4. Totals
scenario          app     n   frames  janky%   p50   p90   p99
## P5. Diagnostics › „Бързодействие“ (the app's own numbers, JankStats)
PERF CARD filled: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 15 | Първи кадър: медиана 3167 мс, последен 1538 мс | Пълно показване: медиана 3976 мс, последно 3060 мс | Топъл старт · 11 | Първи кадър: медиана 656 мс, последен 367 мс | Насечени кадри по екрани | settings: 5,2 % от 77 кадъра · най-бавен 452 мс | more: 2,8 % от 141 кадъра · най-бавен 274 мс | article: 2,5 % от 118 кадъра · най-бавен 663 мс | gallery: 1,1 % от 88 кадъра · най-бавен 340 мс | my: 1,0 % от 96 кадъра · най-бавен 296 мс | Няма записани грешки. | Диагностика
PERF CARD filled (scrolled): Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 15 | Първи кадър: медиана 3167 мс, последен 1538 мс | Пълно показване: медиана 3976 мс, последно 3060 мс | Топъл старт · 11 | Първи кадър: медиана 656 мс, последен 367 мс | Насечени кадри по екрани | settings: 5,2 % от 77 кадъра · най-бавен 452 мс | more: 2,8 % от 141 кадъра · най-бавен 274 мс | article: 2,5 % от 118 кадъра · най-бавен 663 мс | gallery: 1,1 % от 88 кадъра · най-бавен 340 мс | my: 1,0 % от 96 кадъра · най-бавен 296 мс | Няма записани грешки. | Диагностика
contact sheets: 4 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 15 | Първи кадър: медиана 3167 мс, последен 1538 мс | Пълно показване: медиана 3976 мс, последно 3060 мс | Топъл старт · 11 | Първи кадър: медиана 656 мс, последен 367 мс | Насечени кадри по екрани | settings: 5,2 % от 77 кадъра · най-бавен 452
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 15" text="Първи кадър: медиана 3167 мс, последен 1538 мс" text="Пълно показване: медиана 3976 мс, последно 3060 мс" text="Топъл старт · 11" text="Първи кадър: медиана 656 мс, последен 367 мс" text="Насечени к
APP W/E lines (pids ['4778', '7315', '7435', '7509', '7589', '7706', '7773', '7980', '8150', '8320', '8403', '8473', '8536', '8809', '12528']): 147 in 12 groups
      28  E/OpenGLRenderer: Unable to match the desired swap behavior.
      23  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      23  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      17  W/Parcel: Expecting binder but got null!
      15  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      15  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      15  W/Gralloc4: allocator #.x is not supported
       4  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
       4  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#e#-d#-#c#-add#-f#d#ec#d, generation=#)
       1  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
       1  W/g.chyavorec.app: No such thread id for suspend: #
contact sheets: 5 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4778', '7315', '7435', '7509', '7589', '7706', '7773', '7980', '8150', '8320', '8403', '8473', '8536', '8809', '12528']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 76
   skipped-frames events=76 max=114 total=4063
== Davey: 157
   davey events=157 max=3972ms median=949ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## hi28

```
part hi28; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
## HI new-1: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean clean shots dump
installed new-1: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI new-1-1-clean (clean): cold start TotalTime: 1363
IDLE hi-new-1-1-clean-20-35s (15 s, no input): frames +44; cpu=14.4% (ticks 216 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=8.7%, Jit thread pool=3.5%, g.chyavorec.app=1.7%, HeapTaskDaemon=0.2%, DefaultExecutor=0.1%; pid 6518
  new-1-1-clean 20-35s FSTL: 48 frames in the window, 3 group(s) [start×frames(length)]: +0.08s×21(1775ms); +7.86s×14(1029ms); +14.89s×13(989ms); gaps between group starts (s): 7.78 7.03; slowest 342 ms
IDLE hi-new-1-1-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 6518
  new-1-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-new-1-1-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 6518
  new-1-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-1-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
HI new-1-2-clean (clean): cold start TotalTime: 1430
IDLE hi-new-1-2-clean-20-35s (15 s, no input): frames +43; cpu=14.6% (ticks 219 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=8.3%, Jit thread pool=4.3%, g.chyavorec.app=1.7%, DefaultExecutor=0.3%, FrameMetricsAgg=0.1%; pid 6745
  new-1-2-clean 20-35s FSTL: 45 frames in the window, 3 group(s) [start×frames(length)]: +0.07s×17(1367ms); +7.45s×14(951ms); +14.40s×14(921ms); gaps between group starts (s): 7.38 6.95; slowest 328 ms
IDLE hi-new-1-2-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 6745
  new-1-2-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-new-1-2-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 6745
  new-1-2-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-1-2-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
HI new-1-3-shots (shots): cold start TotalTime: 1363
IDLE hi-new-1-3-shots-20-35s (15 s, no input, 17 screenshots inside): frames +43
  CHANGES: 4 of 16 consecutive pairs differ: 02:[0,116][1080,1328] 03:same 04:same 05:same 06:same 07:same 08:same 09:[0,648][1080,1276] 10:[0,648][1068,1328] 11:same 12:same 13:same 14:same 15:same 16:same 17:[0,648][1080,1328]
  new-1-3-shots 20-35s FSTL: 43 frames in the window, 3 group(s) [start×frames(length)]: +0.15s×16(1446ms); +7.61s×14(1039ms); +14.65s×13(1022ms); gaps between group starts (s): 7.46 7.04; slowest 398 ms
IDLE hi-new-1-3-shots-45-60s (15 s, no input, 19 screenshots inside): frames +0
  CHANGES: 0 of 18 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same 19:same
  new-1-3-shots 45-60s FSTL: 0 frames in the window (120 in the buffer; window 16.3 s)
IDLE hi-new-1-3-shots-90-105s (15 s, no input, 19 screenshots inside): frames +0
  CHANGES: 0 of 18 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same 19:same
  new-1-3-shots 90-105s FSTL: 0 frames in the window (120 in the buffer; window 16.4 s)
HI new-1-3-shots screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
HI new-1-4-dump (dump): cold start TotalTime: 1327
IDLE hi-new-1-4-dump-20-35s (15 s, no input): frames +27; cpu=8.9% (ticks 133 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=5.5%, Jit thread pool=2.1%, g.chyavorec.app=1.1%, FrameMetricsAgg=0.1%, DefaultExecutor=0.1%; pid 7648
  new-1-4-dump 20-35s FSTL: 27 frames in the window, 2 group(s) [start×frames(length)]: +5.14s×13(971ms); +12.07s×14(991ms); gaps between group starts (s): 6.93; slowest 350 ms
IDLE hi-new-1-4-dump-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7648
  new-1-4-dump 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.1 s)
IDLE hi-new-1-4-dump-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7648
  new-1-4-dump 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-1-4-dump screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
## HI base-1: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean clean shots dump
installed base-1: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI base-1-1-clean (clean): cold start TotalTime: 1322
IDLE hi-base-1-1-clean-20-35s (15 s, no input): frames +44; cpu=14.3% (ticks 214 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=9.1%, Jit thread pool=3.3%, g.chyavorec.app=1.5%, DefaultExecutor=0.2%, HeapTaskDaemon=0.1%; pid 8294
  base-1-1-clean 20-35s FSTL: 45 frames in the window, 3 group(s) [start×frames(length)]: +0.11s×16(1321ms); +7.44s×15(1029ms); +14.47s×14(890ms); gaps between group starts (s): 7.33 7.03; slowest 348 ms
IDLE hi-base-1-1-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8294
  base-1-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-base-1-1-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8294
  base-1-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI base-1-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
HI base-1-2-clean (clean): cold start TotalTime: 1496
IDLE hi-base-1-2-clean-20-35s (15 s, no input): frames +41; cpu=13.3% (ticks 199 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=8.3%, Jit thread pool=3.0%, g.chyavorec.app=1.7%, DefaultExecutor=0.2%, FrameMetricsAgg=0.1%; pid 8472
  base-1-2-clean 20-35s FSTL: 46 frames in the window, 3 group(s) [start×frames(length)]: +0.07s×17(1413ms); +7.49s×14(1002ms); +14.50s×15(1069ms); gaps between group starts (s): 7.42 7.01; slowest 352 ms
IDLE hi-base-1-2-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8472
  base-1-2-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.1 s)
IDLE hi-base-1-2-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8472
  base-1-2-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI base-1-2-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
HI base-1-3-shots (shots): cold start TotalTime: 1538
IDLE hi-base-1-3-shots-20-35s (15 s, no input, 17 screenshots inside): frames +42
  CHANGES: 4 of 16 consecutive pairs differ: 02:[0,116][1080,1328] 03:same 04:same 05:same 06:same 07:same 08:same 09:[0,648][1080,1276] 10:[0,648][1080,1328] 11:same 12:same 13:same 14:same 15:same 16:same 17:[0,648][1080,1328]
  base-1-3-shots 20-35s FSTL: 42 frames in the window, 3 group(s) [start×frames(length)]: +0.08s×17(1552ms); +7.65s×12(946ms); +14.59s×13(1101ms); gaps between group starts (s): 7.56 6.95; slowest 403 ms
IDLE hi-base-1-3-shots-45-60s (15 s, no input, 19 screenshots inside): frames +0
  CHANGES: 0 of 18 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same 19:same
  base-1-3-shots 45-60s FSTL: 0 frames in the window (120 in the buffer; window 16.3 s)
IDLE hi-base-1-3-shots-90-105s (15 s, no input, 19 screenshots inside): frames +0
  CHANGES: 0 of 18 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same 19:same
  base-1-3-shots 90-105s FSTL: 0 frames in the window (120 in the buffer; window 16.6 s)
HI base-1-3-shots screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
HI base-1-4-dump (dump): cold start TotalTime: 1380
IDLE hi-base-1-4-dump-20-35s (15 s, no input): frames +29; cpu=10.3% (ticks 155 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=6.3%, Jit thread pool=2.9%, g.chyavorec.app=1.1%, DefaultExecutor=0.1%; pid 9319
  base-1-4-dump 20-35s FSTL: 29 frames in the window, 2 group(s) [start×frames(length)]: +5.14s×14(916ms); +12.04s×15(1029ms); gaps between group starts (s): 6.90; slowest 327 ms
IDLE hi-base-1-4-dump-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9319
  base-1-4-dump 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-base-1-4-dump-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9319
  base-1-4-dump 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI base-1-4-dump screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
## HI new-2: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean shots
installed new-2: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI new-2-1-clean (clean): cold start TotalTime: 1516
IDLE hi-new-2-1-clean-20-35s (15 s, no input): frames +41; cpu=14.7% (ticks 220 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=9.7%, Jit thread pool=3.1%, g.chyavorec.app=1.6%, DefaultExecutor=0.1%, FrameMetricsAgg=0.1%; pid 9908
  new-2-1-clean 20-35s FSTL: 46 frames in the window, 3 group(s) [start×frames(length)]: +0.00s×19(1798ms); +7.82s×14(920ms); +14.73s×13(879ms); gaps between group starts (s): 7.82 6.91; slowest 376 ms
IDLE hi-new-2-1-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9908
  new-2-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-new-2-1-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9908
  new-2-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-2-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
HI new-2-2-shots (shots): cold start TotalTime: 1349
IDLE hi-new-2-2-shots-20-35s (15 s, no input, 16 screenshots inside): frames +35
  CHANGES: 3 of 15 consecutive pairs differ: 02:[0,116][1080,1328] 03:same 04:same 05:same 06:same 07:same 08:same 09:[0,648][1080,1276] 10:[0,648][1080,1328] 11:same 12:same 13:same 14:same 15:same 16:same
  new-2-2-shots 20-35s FSTL: 44 frames in the window, 3 group(s) [start×frames(length)]: +0.00s×18(1726ms); +7.74s×13(964ms); +14.71s×13(1017ms); gaps between group starts (s): 7.74 6.97; slowest 418 ms
IDLE hi-new-2-2-shots-45-60s (15 s, no input, 19 screenshots inside): frames +0
  CHANGES: 0 of 18 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same 19:same
  new-2-2-shots 45-60s FSTL: 0 frames in the window (120 in the buffer; window 16.5 s)
IDLE hi-new-2-2-shots-90-105s (15 s, no input, 19 screenshots inside): frames +0
  CHANGES: 0 of 18 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same 19:same
  new-2-2-shots 90-105s FSTL: 0 frames in the window (120 in the buffer; window 16.3 s)
HI new-2-2-shots screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
## HI base-2: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean shots
installed base-2: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI base-2-1-clean (clean): cold start TotalTime: 1337
IDLE hi-base-2-1-clean-20-35s (15 s, no input): frames +42; cpu=12.8% (ticks 192 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=8.9%, Jit thread pool=2.1%, g.chyavorec.app=1.5%, DefaultExecutor=0.2%, FrameMetricsAgg=0.1%; pid 11111
  base-2-1-clean 20-35s FSTL: 46 frames in the window, 3 group(s) [start×frames(length)]: +0.14s×17(1412ms); +7.57s×14(995ms); +14.57s×15(971ms); gaps between group starts (s): 7.43 7.00; slowest 350 ms
IDLE hi-base-2-1-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11111
  base-2-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-base-2-1-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11111
  base-2-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI base-2-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
HI base-2-2-shots (shots): cold start TotalTime: 1207
IDLE hi-base-2-2-shots-20-35s (15 s, no input, 17 screenshots inside): frames +40
  CHANGES: 4 of 16 consecutive pairs differ: 02:[0,116][1080,1328] 03:same 04:same 05:same 06:same 07:same 08:same 09:[0,664][1080,1272] 10:[0,648][1080,1328] 11:same 12:same 13:same 14:same 15:same 16:same 17:[0,648][1080,1328]
  base-2-2-shots 20-35s FSTL: 41 frames in the window, 3 group(s) [start×frames(length)]: +0.02s×15(1308ms); +7.36s×13(900ms); +14.24s×13(1072ms); gaps between group starts (s): 7.34 6.88; slowest 368 ms
IDLE hi-base-2-2-shots-45-60s (15 s, no input, 19 screenshots inside): frames +0
  CHANGES: 0 of 18 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same 19:same
  base-2-2-shots 45-60s FSTL: 0 frames in the window (120 in the buffer; window 16.3 s)
IDLE hi-base-2-2-shots-90-105s (15 s, no input, 19 screenshots inside): frames +0
  CHANGES: 0 of 18 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same 19:same
  base-2-2-shots 90-105s FSTL: 0 frames in the window (120 in the buffer; window 16.5 s)
HI base-2-2-shots screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
## HI totals: frames in each 15 s window (label: build-block-run-mode)
HI-TOTAL base clean 20-35s: 44 41 42
HI-TOTAL base clean 45-60s: 0 0 0
HI-TOTAL base clean 90-105s: 0 0 0
HI-TOTAL base dump 20-35s: 29
HI-TOTAL base dump 45-60s: 0
HI-TOTAL base dump 90-105s: 0
HI-TOTAL base shots 20-35s: 42 40
HI-TOTAL base shots 45-60s: 0 0
HI-TOTAL base shots 90-105s: 0 0
HI-TOTAL new clean 20-35s: 44 43 41
HI-TOTAL new clean 45-60s: 0 0 0
HI-TOTAL new clean 90-105s: 0 0 0
HI-TOTAL new dump 20-35s: 27
HI-TOTAL new dump 45-60s: 0
HI-TOTAL new dump 90-105s: 0
HI-TOTAL new shots 20-35s: 43 35
HI-TOTAL new shots 45-60s: 0 0
HI-TOTAL new shots 90-105s: 0 0
contact sheets: 3 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 3 | Първи кадър: медиана 1180 мс, последен 1154 мс | Пълно показване: медиана 1575 мс, последно 1575 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 3" text="Първи кадър: медиана 1180 мс, последен 1154 мс" text="Пълно показване: медиана 1575 мс, последно 1575 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['6150', '6518', '6745', '6983', '7648', '7998', '8294', '8472', '8670', '9319', '9620', '9908', '10088', '10825', '11111', '11291']): 71 in 6 groups
      20  E/eglCodecCommon: glUtilsParamSize: unknow param #
      16  W/g.chyavorec.ap: Unexpected CPU variant for X# using defaults: x#_#
      16  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      16  E/eglCodecCommon: GoldfishAddressSpaceHostMemoryAllocator: ioctl_ping failed for device_type=#, ret=-#
       2  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#e#cba-#cde-#b#-a#fe-a#fcde#e, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#b#-f#f-#a-ab#-d#d#b#, generation=#)
contact sheets: 4 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['6150', '6518', '6745', '6983', '7648', '7998', '8294', '8472', '8670', '9319', '9620', '9908', '10088', '10825', '11111', '11291']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 8
   skipped-frames events=8 max=40 total=279
== Davey: 16
   davey events=16 max=1060ms median=764ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 83123049 Nov 12 20:46 app-mapping.txt; crashes: 0 B
```

## idle33

```
part idle33; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## HI new-1: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean clean shots dump
installed new-1: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI new-1-1-clean (clean): cold start TotalTime: 4698
IDLE hi-new-1-1-clean-20-35s (15 s, no input): frames +37; cpu=11.5% (ticks 172 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=10.1%, g.chyavorec.app=0.8%, Jit thread pool=0.5%, DefaultExecutor=0.1%; pid 6514
  new-1-1-clean 20-35s FSTL: 38 frames in the window, 12 group(s) [start×frames(length)]: +0.08s×3(383ms); +1.05s×2(17ms); +1.35s×4(583ms); +2.23s×6(900ms); +3.40s×3(50ms); +3.73s×4(533ms); +4.55s×2(117ms); +5.17s×1(0ms); +5.45s×2(17ms); +5.78s×2(200ms); +10.68s×5(167ms); +11.18s×4(583ms); gaps between group starts (s): 0.97 0.30 0.88 1.17 0.33 0.82 0.62 0.28 0.33 4.90 0.50; slowest 772 ms
IDLE hi-new-1-1-clean-45-60s (15 s, no input): frames +0; cpu=0.1% (ticks 1 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 6514
  new-1-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.3 s)
IDLE hi-new-1-1-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 6514
  new-1-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-1-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
HI new-1-2-clean (clean): cold start TotalTime: 3654
IDLE hi-new-1-2-clean-20-35s (15 s, no input): frames +37; cpu=13.8% (ticks 207 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=12.1%, g.chyavorec.app=0.9%, Jit thread pool=0.6%, binder:7827_4=0.1%, DefaultExecutor=0.1%; pid 7827
  new-1-2-clean 20-35s FSTL: 39 frames in the window, 7 group(s) [start×frames(length)]: +0.06s×2(83ms); +0.58s×4(317ms); +1.16s×3(167ms); +1.59s×4(433ms); +2.36s×9(1150ms); +3.86s×7(1083ms); +9.53s×10(1067ms); gaps between group starts (s): 0.52 0.58 0.43 0.77 1.50 5.67; slowest 589 ms
IDLE hi-new-1-2-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7827
  new-1-2-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-new-1-2-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7827
  new-1-2-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-1-2-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
HI new-1-3-shots (shots): cold start TotalTime: 3530
IDLE hi-new-1-3-shots-20-35s (15 s, no input, 11 screenshots inside): frames +34
  CHANGES: 4 of 10 consecutive pairs differ: 02:[0,108][1080,1392] 03:[12,312][1080,384] 04:same 05:same 06:same 07:[0,716][1080,1340] 08:[0,712][1080,1392] 09:same 10:same 11:same
  new-1-3-shots 20-35s FSTL: 44 frames in the window, 12 group(s) [start×frames(length)]: +0.02s×3(50ms); +0.56s×2(17ms); +0.96s×12(1800ms); +3.02s×1(0ms); +3.32s×1(0ms); +3.66s×4(383ms); +4.47s×3(250ms); +9.17s×4(50ms); +9.51s×5(750ms); +16.54s×5(150ms); +17.07s×2(133ms); +17.59s×2(200ms); gaps between group starts (s): 0.53 0.40 2.07 0.30 0.33 0.82 4.70 0.33 7.03 0.53 0.52; slowest 892 ms
IDLE hi-new-1-3-shots-45-60s (15 s, no input, 16 screenshots inside): frames +0
  CHANGES: 0 of 15 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same
  new-1-3-shots 45-60s FSTL: 0 frames in the window (120 in the buffer; window 16.9 s)
IDLE hi-new-1-3-shots-90-105s (15 s, no input, 15 screenshots inside): frames +0
  CHANGES: 0 of 14 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same
  new-1-3-shots 90-105s FSTL: 0 frames in the window (120 in the buffer; window 16.1 s)
HI new-1-3-shots screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
HI new-1-4-dump (dump): cold start TotalTime: 3684
IDLE hi-new-1-4-dump-20-35s (15 s, no input): frames +18; cpu=6.8% (ticks 102 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=5.4%, g.chyavorec.app=0.7%, Jit thread pool=0.6%, Profile Saver=0.1%; pid 8825
  new-1-4-dump 20-35s FSTL: 19 frames in the window, 2 group(s) [start×frames(length)]: +2.57s×9(933ms); +9.67s×10(1167ms); gaps between group starts (s): 7.10; slowest 611 ms
IDLE hi-new-1-4-dump-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8825
  new-1-4-dump 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-new-1-4-dump-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8825
  new-1-4-dump 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-1-4-dump screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
## HI base-1: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean clean shots
installed base-1: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI base-1-1-clean (clean): cold start TotalTime: 3297
IDLE hi-base-1-1-clean-20-35s (15 s, no input): frames +39; cpu=14.0% (ticks 210 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=11.9%, g.chyavorec.app=1.0%, Jit thread pool=0.9%, DefaultExecutor=0.1%, binder:9711_3=0.1%; pid 9711
  base-1-1-clean 20-35s FSTL: 45 frames in the window, 8 group(s) [start×frames(length)]: +0.15s×3(250ms); +0.77s×12(1833ms); +3.05s×1(0ms); +3.32s×9(1100ms); +4.73s×3(283ms); +8.82s×9(900ms); +15.87s×5(250ms); +16.47s×3(150ms); gaps between group starts (s): 0.62 2.28 0.27 1.42 4.08 7.05 0.60; slowest 629 ms
IDLE hi-base-1-1-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9711
  base-1-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-base-1-1-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9711
  base-1-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI base-1-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
HI base-1-2-clean (clean): cold start TotalTime: 3365
IDLE hi-base-1-2-clean-20-35s (15 s, no input): frames +34; cpu=12.7% (ticks 190 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=11.4%, g.chyavorec.app=0.8%, Jit thread pool=0.5%; pid 9895
  base-1-2-clean 20-35s FSTL: 35 frames in the window, 6 group(s) [start×frames(length)]: +0.19s×2(167ms); +0.70s×2(33ms); +1.12s×4(467ms); +1.89s×11(1633ms); +3.80s×6(833ms); +9.29s×10(1167ms); gaps between group starts (s): 0.52 0.42 0.77 1.92 5.48; slowest 661 ms
IDLE hi-base-1-2-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9895
  base-1-2-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-base-1-2-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9895
  base-1-2-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI base-1-2-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
HI base-1-3-shots (shots): cold start TotalTime: 3480
IDLE hi-base-1-3-shots-20-35s (15 s, no input, 12 screenshots inside): frames +33
  CHANGES: 4 of 11 consecutive pairs differ: 02:[0,108][1080,1340] 03:[0,312][1080,1392] 04:[20,316][1024,376] 05:same 06:same 07:same 08:[0,712][1080,1392] 09:same 10:same 11:same 12:same
  base-1-3-shots 20-35s FSTL: 42 frames in the window, 11 group(s) [start×frames(length)]: +0.00s×2(133ms); +0.55s×2(150ms); +0.97s×2(150ms); +1.44s×12(1767ms); +3.57s×3(333ms); +4.19s×2(150ms); +4.79s×1(0ms); +9.22s×5(283ms); +9.79s×4(533ms); +16.59s×5(300ms); +17.19s×4(600ms); gaps between group starts (s): 0.55 0.42 0.47 2.13 0.62 0.60 4.43 0.57 6.80 0.60; slowest 785 ms
IDLE hi-base-1-3-shots-45-60s (15 s, no input, 16 screenshots inside): frames +0
  CHANGES: 0 of 15 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same
  base-1-3-shots 45-60s FSTL: 0 frames in the window (120 in the buffer; window 16.8 s)
IDLE hi-base-1-3-shots-90-105s (15 s, no input, 15 screenshots inside): frames +0
  CHANGES: 0 of 14 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same
  base-1-3-shots 90-105s FSTL: 0 frames in the window (120 in the buffer; window 16.0 s)
HI base-1-3-shots screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
## HI new-2: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean
installed new-2: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI new-2-1-clean (clean): cold start TotalTime: 3741
IDLE hi-new-2-1-clean-20-35s (15 s, no input): frames +36; cpu=13.9% (ticks 208 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=12.2%, g.chyavorec.app=0.8%, Jit thread pool=0.7%, binder:11293_3=0.1%, FrameMetricsAgg=0.1%; pid 11293
  new-2-1-clean 20-35s FSTL: 42 frames in the window, 7 group(s) [start×frames(length)]: +0.09s×3(333ms); +0.79s×12(1767ms); +2.92s×10(1583ms); +8.76s×7(583ms); +9.66s×2(17ms); +15.86s×5(267ms); +16.46s×3(200ms); gaps between group starts (s): 0.70 2.13 5.83 0.90 6.20 0.60; slowest 647 ms
IDLE hi-new-2-1-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11293
  new-2-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-new-2-1-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11293
  new-2-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-2-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
## HI totals: frames in each 15 s window (label: build-block-run-mode)
HI-TOTAL base clean 20-35s: 39 34
HI-TOTAL base clean 45-60s: 0 0
HI-TOTAL base clean 90-105s: 0 0
HI-TOTAL base shots 20-35s: 33
HI-TOTAL base shots 45-60s: 0
HI-TOTAL base shots 90-105s: 0
HI-TOTAL new clean 20-35s: 37 37 36
HI-TOTAL new clean 45-60s: 0 0 0
HI-TOTAL new clean 90-105s: 0 0 0
HI-TOTAL new dump 20-35s: 18
HI-TOTAL new dump 45-60s: 0
HI-TOTAL new dump 90-105s: 0
HI-TOTAL new shots 20-35s: 34
HI-TOTAL new shots 45-60s: 0
HI-TOTAL new shots 90-105s: 0
## I0. CPU and frames at rest on every screen (1.2.35)
## W. First launch and warm-up
first-install catalogue results after 3534 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## I. CPU and frames with no input: 30 s after opening each screen, then a 15 s window (should be ~0 frames, ~0% CPU)
IDLE home (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE catalog (15 s, no input): frames +30; cpu=1.5% (ticks 22 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=0.8%, g.chyavorec.app=0.5%, FrameMetricsAgg=0.1%, DefaultExecutor=0.1%; pid 11877
IDLE catalog-shelf (15 s, no input): frames +31; cpu=1.5% (ticks 23 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=0.8%, g.chyavorec.app=0.5%, binder:11877_6=0.1%, Jit thread pool=0.1%; pid 11877
IDLE book (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE surprise-result (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE home-search-expanded (15 s, no input): frames +31; cpu=1.1% (ticks 17 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=0.7%, g.chyavorec.app=0.3%, binder:11877_7=0.1%, DefaultExecutor=0.1%; pid 11877
IDLE my (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE card (15 s, no input): frames +0; cpu=0.3% (ticks 4 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.3%; pid 11877
IDLE card-full (15 s, no input): frames +0; cpu=0.2% (ticks 3 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.2%; pid 11877
IDLE want (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE news (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE article (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE calendar (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE event (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE more (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE gallery (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE album (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE viewer (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE settings (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE diagnostics (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE contacts (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE documents (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11877
IDLE background (30 s, no input): frames +0; cpu=0.1% (ticks 4 in 30 s; 100 ticks = 1 cpu-s) threads: HeapTaskDaemon=0.1%, ReferenceQueueD=0.0%; pid 11877
## I2. Screens that kept drawing (frames > 0 in the 15 s window)
IDLE hi-new-1-1-clean-20-35s (15 s, no input): frames +37; cpu=11.5% (ticks 172 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=10.1%, g.chyavorec.app=0.8%, Jit thread pool=0.5%, DefaultExecutor=0
IDLE hi-new-1-2-clean-20-35s (15 s, no input): frames +37; cpu=13.8% (ticks 207 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=12.1%, g.chyavorec.app=0.9%, Jit thread pool=0.6%, binder:7827_4=0.1
IDLE hi-new-1-3-shots-20-35s (15 s, no input, 11 screenshots inside): frames +34
IDLE hi-new-1-3-shots-45-60s (15 s, no input, 16 screenshots inside): frames +0
IDLE hi-new-1-3-shots-90-105s (15 s, no input, 15 screenshots inside): frames +0
IDLE hi-new-1-4-dump-20-35s (15 s, no input): frames +18; cpu=6.8% (ticks 102 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=5.4%, g.chyavorec.app=0.7%, Jit thread pool=0.6%, Profile Saver=0.1%; 
IDLE hi-base-1-1-clean-20-35s (15 s, no input): frames +39; cpu=14.0% (ticks 210 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=11.9%, g.chyavorec.app=1.0%, Jit thread pool=0.9%, DefaultExecutor=
IDLE hi-base-1-2-clean-20-35s (15 s, no input): frames +34; cpu=12.7% (ticks 190 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=11.4%, g.chyavorec.app=0.8%, Jit thread pool=0.5%; pid 9895
IDLE hi-base-1-3-shots-20-35s (15 s, no input, 12 screenshots inside): frames +33
IDLE hi-base-1-3-shots-45-60s (15 s, no input, 16 screenshots inside): frames +0
IDLE hi-base-1-3-shots-90-105s (15 s, no input, 15 screenshots inside): frames +0
IDLE hi-new-2-1-clean-20-35s (15 s, no input): frames +36; cpu=13.9% (ticks 208 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=12.2%, g.chyavorec.app=0.8%, Jit thread pool=0.7%, binder:11293_3=0.
IDLE catalog (15 s, no input): frames +30; cpu=1.5% (ticks 22 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=0.8%, g.chyavorec.app=0.5%, FrameMetricsAgg=0.1%, DefaultExecutor=0.1%; pid 11877
IDLE catalog-shelf (15 s, no input): frames +31; cpu=1.5% (ticks 23 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=0.8%, g.chyavorec.app=0.5%, binder:11877_6=0.1%, Jit thread pool=0.1%; pid 11877
IDLE home-search-expanded (15 s, no input): frames +31; cpu=1.1% (ticks 17 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=0.7%, g.chyavorec.app=0.3%, binder:11877_7=0.1%, DefaultExecutor=0.1%; pi
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 3 | Първи кадър: медиана 3336 мс, последен 3336 мс | Пълно показване: медиана 3851 мс, последно 3851 мс | Топъл старт · 1 | Първи кадър: медиана 856 мс, последен 856 мс | Насечени кадри по екрани | my: 13,9 % от 36 кадъра · най-бавен 364 мс | m
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 3" text="Първи кадър: медиана 3336 мс, последен 3336 мс" text="Пълно показване: медиана 3851 мс, последно 3851 мс" text="Топъл старт · 1" text="Първи кадър: медиана 856 мс, последен 856 мс" text="Насечени кад
APP W/E lines (pids ['5202', '6514', '7827', '8082', '8825', '9413', '9711', '9895', '10150', '10976', '11293', '11877']): 116 in 14 groups
      14  E/OpenGLRenderer: Unable to match the desired swap behavior.
      13  W/Parcel: Expecting binder but got null!
      13  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      13  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      12  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      12  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      12  W/Gralloc4: allocator #.x is not supported
      10  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
       9  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       2  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object ch#.getValue()(r#-map-id
       2  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=cf#b#bd#-dc#b-#e#-#d#-b#dcd, generation=#)
       2  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#ef-cdc#-#abd-#-d#a#c#de, generation=#)
       1  W/g.chyavorec.app: Long monitor contention with owner ConnectivityThread (#) at void ad.U(yv#, java.lang.Runnable)(r#-map-id-#a#f
contact sheets: 6 in sheets/
contact sheets: 6 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5202', '6514', '7827', '8082', '8825', '9413', '9711', '9895', '10150', '10976', '11293', '11877']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 68
   skipped-frames events=68 max=116 total=3707
== Davey: 139
   davey events=139 max=3092ms median=938ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## land35

```
part land35; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15700 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3522 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## LD1. Landscape (rotation applied after each restart): the whole tour, light and dark
TOUR_ROT=1: screen now 2400x1080
## T-landscape-light. All screens (2400x1080 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 1)
CAP landscape-light/01-home: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=1 [Търси книга, новина, събитие…] | Начало · Каталог · Моето · Новини · Още · НЧ „Васил Левски – 1922“
CAP landscape-light/01b-home-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=1 [Търси книга, новина, събитие…] | Начало · Каталог · Моето · Новини · Още · Лента за търсене
CAP landscape-light/01c-home-3: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Последни новини
CAP landscape-light/01d-home-4: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Последни новини
CAP landscape-light/01e-search-12: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP landscape-light/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP landscape-light/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP landscape-light/02c-filters-2: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP landscape-light/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP landscape-light/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP landscape-light/02f-shelf-2: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP landscape-light/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP landscape-light/03-book: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP landscape-light/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP landscape-light/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP landscape-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Вила в Прованс  · Още една · Към книгата · Затвори
CAP landscape-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP landscape-light/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP landscape-light/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP landscape-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP landscape-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP landscape-light/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP landscape-light/08b-news-2: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP landscape-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP landscape-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP landscape-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP landscape-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP landscape-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP landscape-light/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP landscape-light/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP landscape-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP landscape-light/12-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP landscape-light/12b-more-2: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=22 ellipsized=0 | Съобщения и покани от читали · Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия
CAP landscape-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP landscape-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP landscape-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP landscape-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP landscape-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP landscape-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP landscape-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP landscape-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP landscape-light/18-settings: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP landscape-light/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български · Сезонни украси
CAP landscape-light/18c-settings-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известия · Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два 
CAP landscape-light/18d-settings-4: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Корици по ISBN от covers.ope · Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни · Новините, събитията, каталог · Поверителност
WARN: settings item =Размер на текста|=Text size not found
CAP landscape-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP landscape-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP landscape-light/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP landscape-light/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP landscape-light/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP landscape-light/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP landscape-light/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 2
CAP landscape-light/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 2
CAP landscape-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP landscape-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP landscape-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR landscape-light: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=3 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=14
TOUR_ROT=1: screen now 2400x1080
## T-landscape-dark. All screens (2400x1080 px, density 420, font_scale 1.0, night yes, app locale [bg-BG], rotation 1)
CAP landscape-dark/01-home: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=1 [Търси книга, новина, събитие…] | Начало · Каталог · Моето · Новини · Още · НЧ „Васил Левски – 1922“
CAP landscape-dark/01b-home-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=1 [Търси книга, новина, събитие…] | Начало · Каталог · Моето · Новини · Още · Лента за търсене
CAP landscape-dark/01c-home-3: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Последни новини
CAP landscape-dark/01d-home-4: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Последни новини
CAP landscape-dark/01e-search-12: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP landscape-dark/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP landscape-dark/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP landscape-dark/02c-filters-2: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP landscape-dark/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP landscape-dark/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP landscape-dark/02f-shelf-2: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP landscape-dark/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP landscape-dark/03-book: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP landscape-dark/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP landscape-dark/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP landscape-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Далечният изгре · Още една · Към книгата · Затвори
CAP landscape-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP landscape-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP landscape-dark/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP landscape-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP landscape-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP landscape-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP landscape-dark/08b-news-2: targets=21 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP landscape-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP landscape-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP landscape-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP landscape-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP landscape-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP landscape-dark/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP landscape-dark/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP landscape-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP landscape-dark/12-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP landscape-dark/12b-more-2: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=22 ellipsized=0 | Съобщения и покани от читали · Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия
CAP landscape-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP landscape-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP landscape-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP landscape-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP landscape-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP landscape-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP landscape-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP landscape-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP landscape-dark/18-settings: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP landscape-dark/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български · Сезонни украси
CAP landscape-dark/18c-settings-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Разклати телефона в каталога · Известия · Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване
CAP landscape-dark/18d-settings-4: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Данни · Корици от интернет · Корици по ISBN от covers.ope · Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни
CAP landscape-dark/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP landscape-dark/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP landscape-dark/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP landscape-dark/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP landscape-dark/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP landscape-dark/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP landscape-dark/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP landscape-dark/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 3
CAP landscape-dark/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 3
CAP landscape-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP landscape-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP landscape-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR landscape-dark: A11Y AGG: 57 screens analysed, 0 dumps failed; totals MISSING=5 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=14
## LD2. The card and the fullscreen card in landscape (scrolled to the button)
landscape: no 'Покажи картата' on the card screen: text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" content-desc="Назад" text="Читателска карта" 
## LD3. Home at rest after 30 s (frames)
IDLE land-home-30s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 13046
A11Y AGG: 113 screens analysed, 0 dumps failed; totals MISSING=8 DOUBLE=0 SAME-LABEL=10 SMALL(<48dp)=28
== MISSING: 5 distinct
   View@[373,0][2183,7]  [1 screens: 01d-home-4]
   View@[0,2040][1080,2127]  [1 screens: 08b-news-2]
   View@[556,296][1038,349]  [1 screens: 12b-more-2]
   View@[42,2321][524,2400]  [1 screens: 13-gallery]
   View@[556,2321][1038,2400]  [1 screens: 13-gallery]
== SMALL: 7 distinct
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   'Ключова дума'  [4 screens: 02-catalog, 02e-shelf, 02f-shelf-2, 02g-search-12]
   'Всички'  [1 screens: 08b-news-2]
   'Запазени'  [1 screens: 08b-news-2]
   'Библиотека'  [1 screens: 08b-news-2]
   'Съобщения и покани от читалище'  [1 screens: 12b-more-2]
   ''  [1 screens: 12b-more-2]
== SAME-LABEL: 1 distinct
   'Манипулатор за преместване с п' x2  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
== DOUBLE: 0 distinct
contact sheets: 14 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 3 | Първи кадър: медиана 1888 мс, последен 1410 мс | Пълно показване: медиана 2391 мс, последно 2391 мс | Топъл старт · 2 | Първи кадър: медиана 990 мс, последен 965 мс | Насечени кадри по екрани | home: 1,9 % от 522 кадъра · най-бавен 1420 мс 
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 3" text="Първи кадър: медиана 1888 мс, последен 1410 мс" text="Пълно показване: медиана 2391 мс, последно 2391 мс" text="Топъл старт · 2" text="Първи кадър: медиана 990 мс, последен 965 мс" text="Насечени кад
APP W/E lines (pids ['5553', '7555', '13046']): 339 in 84 groups
      26  E/OpenGLRenderer: Unable to match the desired swap behavior.
      20  W/Parcel: Expecting binder but got null!
       7  W/BasicSecureTextField: 	at o#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       6  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       6  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       5  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       5  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       4  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       4  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       4  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       4  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       4  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       4  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       4  W/BasicSecureTextField: 	at h#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at vy#.b(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at vy#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at jr#.invoke(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at sn#.c(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at sn#.invoke(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at g#.b(Unknown Source:#)
       4  W/BasicSecureTextField: 	at kr#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at a#.f(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at sn#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at sn#.f(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at d#.invoke(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at al#.x(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at u#.o(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at gq#.j(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at dc#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
contact sheets: 15 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5553', '7555', '13046']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 22
   skipped-frames events=22 max=74 total=886
== Davey: 56
   davey events=56 max=2764ms median=844ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 83123049 Nov 12 21:43 app-mapping.txt; crashes: 0 B
```

## mem

```
part mem; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2897 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), new
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=58707KB  Java=8052KB  Native=24324KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=120480KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=65339KB  Java=8580KB  Native=24116KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=127768KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c05 (trimmed)    PSS=95819KB  Java=12424KB  Native=41352KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=25)  RSS=168272KB  Views=13 Activities=1
cycles done: 10, gallery viewer reached in 10, search expanded in 10
MEM c10 (raw)        PSS=119103KB  Java=17276KB  Native=46212KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=19)  RSS=172856KB  Views=20 Activities=1
MEM c10 (trimmed)    PSS=109547KB  Java=11296KB  Native=42624KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=19)  RSS=163388KB  Views=13 Activities=1
MEM c15 (trimmed)    PSS=115773KB  Java=11372KB  Native=47756KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=171272KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20, search expanded in 20
MEM c20 (raw)        PSS=137430KB  Java=23284KB  Native=56948KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=193968KB  Views=20 Activities=1
MEM c20 (trimmed)    PSS=118995KB  Java=11396KB  Native=50432KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=175532KB  Views=13 Activities=1
MEM c25 (trimmed)    PSS=120344KB  Java=11460KB  Native=51656KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=24)  RSS=177696KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30, search expanded in 30
MEM c30 (raw)        PSS=136959KB  Java=22456KB  Native=57740KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=24)  RSS=195884KB  Views=20 Activities=1
MEM c30 (trimmed)    PSS=119076KB  Java=11424KB  Native=51116KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=24)  RSS=178000KB  Views=13 Activities=1
pid after cycles: 7099; gallery viewer reached in 30/30, expanded search with '12' in 30/30 cycles
PSS after trim by cycle (KB): c0=65339 c5=95819 c10=109547 c15=115773 c20=118995 c25=120344 c30=119076
LEAK TREND (cycles 10-30, least squares): +473 KB per cycle (+9.2 MB over 20 cycles)
MEMTABLE new 1.2.35 (MB; Graphics = GL mtrack + EGL mtrack + Gfx dev in the App Summary)
  c00 raw  PSS=57.3  Java=7.9  Native=23.8  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.6  PrivOther=3.9  RSS=117.7
  c00 trim PSS=63.8  Java=8.4  Native=23.6  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.6  PrivOther=9.7  RSS=124.8
  c10 raw  PSS=116.3  Java=16.9  Native=45.1  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.3  PrivOther=9.1  RSS=168.8
  c10 trim PSS=107.0  Java=11.0  Native=41.6  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.3  PrivOther=9.0  RSS=159.6
  c20 raw  PSS=134.2  Java=22.7  Native=55.6  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.3  PrivOther=9.0  RSS=189.4
  c20 trim PSS=116.2  Java=11.1  Native=49.2  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.3  PrivOther=8.9  RSS=171.4
  c30 raw  PSS=133.7  Java=21.9  Native=56.4  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.3  PrivOther=9.2  RSS=191.3
  c30 trim PSS=116.3  Java=11.2  Native=49.9  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.3  PrivOther=9.0  RSS=173.8
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 2 | Първи кадър: медиана 1589 мс, последен 1672 мс | Пълно показване: медиана 1895 мс, последно 2284 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 2" text="Първи кадър: медиана 1589 мс, последен 1672 мс" text="Пълно показване: медиана 1895 мс, последно 2284 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['5503', '7099']): 109 in 8 groups
      32  W/Parcel: Expecting binder but got null!
      32  E/OpenGLRenderer: Unable to match the desired swap behavior.
      19  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      19  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       2  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       2  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       2  W/Gralloc4: allocator #.x is not supported
       1  W/g.chyavorec.app: Cleared Reference was only reachable from finalizer (only reported once)
contact sheets: 1 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5503', '7099']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 51
   skipped-frames events=51 max=57 total=1799
== Davey: 154
   davey events=154 max=1386ms median=814ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## monkey

```
part monkey; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3505 ms
## K-1357. Monkey 8000 events (seed 1357, throttle 80 ms, CAMERA revoked)
MONKEY 1357 exit=0 duration=610 s; Events injected: 8000 // Monkey finished 
MONKEY 1357 markers: CRASH=0 ANR=0 FATAL since start: 0
MEM after-monkey-1357 (raw) PSS=118470KB  Java=19956KB  Native=18840KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=8)  RSS=137136KB  Views=42 Activities=1
MEM after-monkey-1357 (trimmed) PSS=93697KB  Java=15980KB  Native=18724KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=8)  RSS=131672KB  Views=22 Activities=1
## K-24680. Monkey 8000 events (seed 24680, throttle 80 ms, CAMERA revoked)
MONKEY 24680 exit=0 duration=431 s; Events injected: 8000 // Monkey finished 
MONKEY 24680 markers: CRASH=0 ANR=0 FATAL since start: 0
MEM after-monkey-24680 (raw) PSS=102778KB  Java=21496KB  Native=51136KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=14)  RSS=195700KB  Views=23 Activities=1
MEM after-monkey-24680 (trimmed) PSS=97143KB  Java=14736KB  Native=47780KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=14)  RSS=190504KB  Views=14 Activities=1
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 3 | Първи кадър: медиана 2416 мс, последен 1470 мс | Пълно показване: медиана 2416 мс, последно 2140 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 3" text="Първи кадър: медиана 2416 мс, последен 1470 мс" text="Пълно показване: медиана 2416 мс, последно 2140 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['5176', '6646', '9306']): 5448 in 61 groups
    4775  W/InputEventSender: Received 'finished' signal for unknown seq number = #
     176  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
     131  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
      62  W/Parcel: Expecting binder but got null!
      62  E/OpenGLRenderer: Unable to match the desired swap behavior.
      27  W/VRI[MainActivity]: Cancelling event (no window focus):KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_UP, scanCode=#, metaState
      17  W/InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
      17  W/RemoteInputConnectionImpl: setComposingRegion on inactive InputConnection
      14  W/VRI[MainActivity]: Cancelling event (no window focus):KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, metaSta
      12  W/System: A resource failed to call release. 
      12  W/VRI[MainActivity]: Dropping event (no window focus):KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_UP, scanCode=#, metaState
      12  W/RemoteInputConnectionImpl: setComposingText on inactive InputConnection
      11  E/ImeBackDispatcher: Ime callback not found. Ignoring unregisterReceivedCallback. callbackId: #
       7  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       7  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       7  W/VRI[MainActivity]: Dropping event (no window focus):KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, metaSta
       6  W/VRI[MainActivity]: Dropping event (no window focus):MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=-#.#, y[#]=-#
       6  W/VRI[MainActivity]: Dropping event due to root view being removed: MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]
       6  W/VRI[MainActivity]: Cancelling event (no window focus):KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_CENTER, scanCode=#, metaS
       4  W/VRI[MainActivity]: Dropping event (no window focus):MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=#.#, y[#]=#.#
       4  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_UP, scanCode=
       4  W/VRI[MainActivity]: Dropping event (no window focus):KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_CENTER, scanCode=#, metaS
       4  W/RemoteInputConnectionImpl: commitText on inactive InputConnection
       3  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       3  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       3  W/Gralloc4: allocator #.x is not supported
       3  E/ViewRootImpl: Calling showInsets(#,true) on window that no longer has views.
       3  W/InputMethodManager: Ignoring showSoftInput() as view=j#{db#edd VFED..... .F...... #,#-#,# aid=#} is not served.
       3  W/VRI[MainActivity]: Cancelling event (no window focus):KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, metaSt
contact sheets: 1 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5176', '6646', '9306']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 509
   skipped-frames events=509 max=165 total=21024
== Davey: 1266
   davey events=1266 max=3678ms median=923ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## offline

```
part offline; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## O1. Online warm-up (fills the caches)
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3265 ms
online-warm home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
online-warm catalogue: results (2446, 2732 ms)
online-warm catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" text="12 -те вятъра" text="Гарф
online-warm book: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 09.11.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" text="Език. Литература (УДК 8)" 
online-warm My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="1 книга в спи
online-warm Want to read: text="Наличност към 09.11.2026" text="Плъзни книга настрани, за да я премахнеш." text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text="Искам да прочета" content-desc="Изчисти списъка" 
online-warm news: text="Новини" text="Календар" text="Търси в новините" text="Всички" text="Запазени" text="Библиотека" text="Събитие" text="БИБЛИОТЕКА  ·  11 ноември 2026" text="Четем заедно – мъдростта на поколенията" text="НЧ „Васил Левски – 1922“ – с. Яворец се включва в инициативите, вдъхновени от Деня на четене
online-warm article: content-desc="Четем заедно – мъдростта на поколенията" text="БИБЛИОТЕКА  ·  11 ноември 2026" text="Четем заедно – мъдростта на поколенията" text="НЧ „Васил Левски – 1922“ – с. Яворец се включва в инициативите, посветени на Деня на четенето и Деня на европейските автори с посланието #ReadForReal.&#10
online-warm calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="16" text="НОЕ" text="80 г. от създаването на ЮНЕСКО" text="Организацията на ООН за образование, наука и култура е основана 
online-warm More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
online-warm gallery: text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" text="5 снимки" text="Село Яворец" text="7 снимки" text="Природата край Яворец" content-desc="Назад" text="Гал
online-warm album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Стопанският 
online-warm contacts: text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читалището" text="Сайт" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица 
online-warm documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O2. Offline cold start with caches: all tabs
offline: airplane=1
offline-cached home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
offline-cached catalogue: results (2446, 2520 ms)
offline-cached catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" text="12 -те вятъра" text="Гарф
offline-cached book: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 09.11.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" text="Език. Литература (УДК 8)" 
offline-cached My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="1 книга в спи
offline-cached Want to read: text="Наличност към 09.11.2026" text="Плъзни книга настрани, за да я премахнеш." text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text="Искам да прочета" content-desc="Изчисти списъка" text="Няма интернет връзка" 
offline-cached news: text="Новини" text="Календар" text="Търси в новините" text="Всички" text="Запазени" text="Библиотека" text="Събитие" text="БИБЛИОТЕКА  ·  11 ноември 2026" text="Четем заедно – мъдростта на поколенията" text="НЧ „Васил Левски – 1922“ – с. Яворец се включва в инициативите, вдъхновени от Деня на четене
offline-cached article: content-desc="Четем заедно – мъдростта на поколенията" text="БИБЛИОТЕКА  ·  11 ноември 2026" text="Четем заедно – мъдростта на поколенията" text="НЧ „Васил Левски – 1922“ – с. Яворец се включва в инициативите, посветени на Деня на четенето и Деня на европейските автори с посланието #ReadForReal.&#10
offline-cached calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Няма интернет връзка. Показваме последно наличните данни." text="Последна синхронизация: Преди 5 минути" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="16" text="НО
offline-cached More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
offline-cached gallery: text="Няма интернет връзка. Показваме последно наличните данни." text="Последна синхронизация: Преди 2 минути" text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“"
offline-cached album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Стопанският 
offline-cached contacts: text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читалището" text="Сайт" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица 
offline-cached documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O3. Reconnect on Contacts (no touch), then the other screens
contacts +10s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читалището" text="Сайт" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец,
contacts +25s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читалището" text="Сайт" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец,
recovered home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="12 ноември 2026 · Св. Йоан Милостиви, патр. Александрийски. Преп. Нил Постник. Преп. Нил Мироточиви, Атонски. Св. прор. 
recovered catalogue: results (2446, 2812 ms)
recovered catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" text="12 -те вятъра" text="Гарф
recovered book: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 09.11.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" text="Език. Литература (УДК 8)" 
recovered My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="1 книга в спи
recovered Want to read: text="Наличност към 09.11.2026" text="Плъзни книга настрани, за да я премахнеш." text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text="Искам да прочета" content-desc="Изчисти списъка" 
recovered news: text="Новини" text="Календар" text="Търси в новините" text="Всички" text="Запазени" text="Библиотека" text="Събитие" text="БИБЛИОТЕКА  ·  11 ноември 2026" text="Четем заедно – мъдростта на поколенията" text="НЧ „Васил Левски – 1922“ – с. Яворец се включва в инициативите, вдъхновени от Деня на четене
recovered article: content-desc="Четем заедно – мъдростта на поколенията" text="БИБЛИОТЕКА  ·  11 ноември 2026" text="Четем заедно – мъдростта на поколенията" text="НЧ „Васил Левски – 1922“ – с. Яворец се включва в инициативите, посветени на Деня на четенето и Деня на европейските автори с посланието #ReadForReal.&#10
recovered calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="16" text="НОЕ" text="80 г. от създаването на ЮНЕСКО" text="Организацията на ООН за образование, наука и култура е основана 
recovered More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
recovered gallery: text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" text="5 снимки" text="Село Яворец" text="7 снимки" text="Природата край Яворец" content-desc="Назад" text="Гал
recovered album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Стопанският 
recovered contacts: text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читалището" text="Сайт" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица 
recovered documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O4. Empty cache, offline, guest: all tabs (MyHub)
offline: airplane=1
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
MY offline guest (empty cache): add-card shown; note shown; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Добави читателска карта" text="Не можем да проверим дали онлайн входът е включен." text="Опитай отново" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text
offline-empty home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" text="Сканирай" text="Читателска 
offline-empty catalogue: NO results
offline-empty catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" text="Каталог" content-desc="Изненадай ме" content-desc="Полица" content-desc=
offline-empty book: text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" content-desc="Назад" text="Няма интернет връзка" 
offline-empty My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Добави читателска карта" text="Не можем да проверим дали онлайн входът е включен." text="Опитай отново" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text
offline-empty Want to read: text="Списъкът е празен" text="Отбележи книга с „Искам да прочета“ на страницата ѝ в каталога — тук ще виждаш дали е на рафта." content-desc="Назад" text="Искам да прочета" text="Няма интернет връзка" 
offline-empty news: text="Новини" text="Календар" text="Търси в новините" text="Всички" text="Запазени" text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" text="Новини и събития" content-desc="Търсене" text="Няма интернет връзка" text="Начало" text="Каталог" text="Моето" 
offline-empty calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" text="Новини и събития" content-desc="Търсене" text="Няма интернет връзка" text="Начало" tex
offline-empty More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
offline-empty gallery: text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" content-desc="Назад" text="Галерия" text="Няма интернет връзка" 
offline-empty contacts: text="Контактите от сайта не се заредиха — показваме основните данни, вградени в приложението. Може да не са актуални." text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читал
offline-empty documents: text="Документи" text="Исторически публикации" text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" content-desc="Назад" text="Документи и архив" text="Няма интернет връзка" 
## O5. Reconnect with the empty cache: Gallery (no touch), then all tabs
GALLERY (empty cache) reconnect: albums shown after 8745 ms (-1 = not within 60 s); text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" 
recovered-empty home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" content-desc="Последни новини" text="БИБЛИОТЕКА" text="Четем заедно – мъдростта на поколенията" text="11 ноември 2026" text="С
recovered-empty catalogue: results (2446, 2983 ms)
recovered-empty catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" text="12 -те вятъра" text="Гарф
recovered-empty book: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 09.11.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" text="Език. Литература (УДК 8)" 
recovered-empty My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="Книги, които 
recovered-empty Want to read: text="Списъкът е празен" text="Отбележи книга с „Искам да прочета“ на страницата ѝ в каталога — тук ще виждаш дали е на рафта." content-desc="Назад" text="Искам да прочета" 
recovered-empty news: text="Новини" text="Календар" text="Търси в новините" text="Всички" text="Запазени" text="Библиотека" text="Събитие" text="БИБЛИОТЕКА  ·  11 ноември 2026" text="Четем заедно – мъдростта на поколенията" text="НЧ „Васил Левски – 1922“ – с. Яворец се включва в инициативите, вдъхновени от Деня на четене
recovered-empty article: content-desc="Четем заедно – мъдростта на поколенията" text="БИБЛИОТЕКА  ·  11 ноември 2026" text="Четем заедно – мъдростта на поколенията" text="НЧ „Васил Левски – 1922“ – с. Яворец се включва в инициативите, посветени на Деня на четенето и Деня на европейските автори с посланието #ReadForReal.&#10
recovered-empty calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="16" text="НОЕ" text="80 г. от създаването на ЮНЕСКО" text="Организацията на ООН за образование, наука и култура е основана 
recovered-empty More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
recovered-empty gallery: text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" text="5 снимки" text="Село Яворец" text="7 снимки" text="Природата край Яворец" content-desc="Назад" text="Гал
recovered-empty album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Стопанският 
recovered-empty contacts: text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читалището" text="Сайт" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица 
recovered-empty documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O6. My offline as guest again (after the online start)
offline: airplane=1
MY offline guest #2: add-card MISSING; note absent; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="Книги, които 
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 2 | Първи кадър: медиана 1824 мс, последен 1709 мс | Пълно показване: медиана 2038 мс, последно 2138 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 2" text="Първи кадър: медиана 1824 мс, последен 1709 мс" text="Пълно показване: медиана 2038 мс, последно 2138 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['4842', '8885', '11626', '14912']): 31 in 10 groups
       4  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       4  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       4  W/Parcel: Expecting binder but got null!
       4  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       4  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       4  W/Gralloc4: allocator #.x is not supported
       4  E/OpenGLRenderer: Unable to match the desired swap behavior.
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=b#d#e#-e#da-#b#-#c#-#c#c#d#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=a#a#-c#-#-#bd-#fe#d#b#b#, generation=#)
       1  W/g.chyavorec.app: No such thread id for suspend: #
contact sheets: 11 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4842', '8885', '11626', '14912']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 10
   skipped-frames events=10 max=71 total=479
== Davey: 20
   davey events=20 max=1655ms median=946ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## robust

```
part robust; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4209 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## RB. Rotation (portrait → landscape → portrait) and process death (Home, am kill, launcher) on every tab and the deep screens
ROT home: pid 5313→5313 (same); landscape: kept 48% of 27 texts; lost e.g. ['Всички', 'СЪБИТИЕ', 'Контакти', 'КАЛЕНДАР']; back to portrait: kept 89% of 27 texts; lost e.g. ['СЪБИТИЕ', '27 септември 2026', 'Празника „38 читалища. Един де']
PROCDEATH home: killed yes; new pid 7931; restored: kept 89% of 27 texts; lost e.g. ['СЪБИТИЕ', '27 септември 2026', 'Празника „38 читалища. Един де']; front app
ROT catalog: pid 7931→7931 (same); landscape: kept 65% of 37 texts; lost e.g. ['Белият пътя', 'Белият дома', 'Ключова дума', 'Елена Русева']; back to portrait: kept 100% of 37 texts
PROCDEATH catalog: killed yes; new pid 8389; restored: kept 100% of 37 texts; front app
ROT my: pid 8389→8389 (same); landscape: kept 65% of 20 texts; lost e.g. ['Известия', 'Моето членство', '1 книга в списъка', 'Статус и валидност']; back to portrait: kept 100% of 20 texts
PROCDEATH my: killed yes; new pid 8763; restored: kept 100% of 20 texts; front app
ROT news: pid 8763→8763 (same); landscape: kept 68% of 19 texts; lost e.g. ['СЪБИТИЕ  ·  16 октомври 2026', 'БИБЛИОТЕКА  ·  11 ноември 2026', 'Четем заедно – мъдростта на по', 'Фолклорно пътешествие из Пирин']; back to portrait: kept 100% of 19 texts
PROCDEATH news: killed yes; new pid 9067; restored: kept 100% of 19 texts; front app
ROT more: pid 9067→9067 (same); landscape: kept 83% of 23 texts; lost e.g. ['Контакти', 'Настройки', 'ПРИЛОЖЕНИЕТО', 'Адрес, телефон, местоположение']; back to portrait: kept 100% of 23 texts
PROCDEATH more: killed yes; new pid 9406; restored: kept 100% of 23 texts; front app
ROT calendar: pid 9406→9406 (same); landscape: kept 72% of 29 texts; lost e.g. ['ДЕК', 'Никулден', 'НАРОДЕН ОБИЧАЙ', 'Международен ден на инвалида']; back to portrait: kept 100% of 29 texts
PROCDEATH calendar: killed yes; new pid 9917; restored: kept 100% of 29 texts; front app
ROT book: pid 9917→9917 (same); landscape: kept 38% of 24 texts; lost e.g. ['Вид', 'Език', 'книга', 'Отдел']; back to portrait: kept 100% of 24 texts
PROCDEATH book: killed yes; new pid 10182; restored: kept 100% of 24 texts; front app
ROT want: pid 10182→10182 (same); landscape: kept 100% of 9 texts; back to portrait: kept 100% of 9 texts
PROCDEATH want: killed yes; new pid 10598; restored: kept 100% of 9 texts; front app
ROT article: pid 10598→10598 (same); landscape: kept 78% of 9 texts; lost e.g. ['СЪБИТИЕ  ·  25 юли 2026', 'Групата за народни танци при Н']; back to portrait: kept 100% of 9 texts
PROCDEATH article: killed yes; new pid 10917; restored: kept 100% of 9 texts; front app
ROT event: pid 10917→10917 (same); landscape: kept 75% of 12 texts; lost e.g. ['Маршрут', 'Напомни ми', 'Виж публикацията']; back to portrait: kept 100% of 12 texts
PROCDEATH event: killed yes; new pid 11357; restored: kept 100% of 12 texts; front app
ROT gallery: pid 11357→11357 (same); landscape: kept 77% of 13 texts; lost e.g. ['5 снимки', 'Село Яворец', 'Природата край Яворец']; back to portrait: kept 100% of 13 texts
PROCDEATH gallery: killed yes; new pid 11842; restored: kept 100% of 13 texts; front app
ROT album: pid 11842→11842 (same); landscape: kept 100% of 20 texts; back to portrait: kept 100% of 20 texts
PROCDEATH album: killed yes; new pid 12311; restored: kept 100% of 20 texts; front app
ROT viewer: pid 12311→12311 (same); landscape: kept 100% of 6 texts; back to portrait: kept 100% of 6 texts
PROCDEATH viewer: killed yes; new pid 12800; restored: kept 100% of 6 texts; front app
ROT settings: pid 12800→12800 (same); landscape: kept 53% of 19 texts; lost e.g. ['Известия', 'Обновления', 'Версия 1.2.35', 'Избери известията']; back to portrait: kept 100% of 19 texts
PROCDEATH settings: killed yes; new pid 13236; restored: kept 100% of 19 texts; front app
ROT diagnostics: pid 13236→13236 (same); landscape: kept 71% of 17 texts; lost e.g. ['Няма записани грешки.', 'my: 8,5 % от 59 кадъра · най-б', 'album: 8,3 % от 36 кадъра · на', 'article: 7,9 % от 38 кадъра · ']; back to portrait: kept 100% of 17 texts
PROCDEATH diagnostics: killed yes; new pid 13781; restored: kept 82% of 17 texts; lost e.g. ['Студен старт · 14', 'more: 11,4 % от 114 кадъра · н', 'Първи кадър: медиана 1652 мс, ']; front app
ROT card: pid 13781→13781 (same); landscape: kept 36% of 14 texts; lost e.g. ['Промени', 'Премахни', 'Покажи картата', 'Не предлагай отново']; back to portrait: kept 100% of 14 texts
PROCDEATH card: killed yes; new pid 14186; restored: kept 100% of 14 texts; front app
ROT search: pid 14186→14186 (same); landscape: kept 60% of 30 texts; lost e.g. ['Белият пътя', 'Белият дома', 'Мартин Тонев', 'Белият здрача']; back to portrait: kept 100% of 30 texts
PROCDEATH search: killed yes; new pid 14620; restored: kept 0% of 30 texts; lost e.g. ['·', 'Книги', 'Назад', 'Всички']; front OTHER
ROT surprise: pid 14620→14620 (same); landscape: kept 50% of 4 texts; lost e.g. ['Още една', 'Към книгата']; back to portrait: kept 100% of 4 texts
PROCDEATH surprise: killed yes; new pid 15046; restored: kept 100% of 4 texts; front app
ROT contacts: pid 15046→15046 (same); landscape: kept 54% of 26 texts; lost e.g. ['В интернет', 'Работно време', 'Неделя: Почивен ден', 'Събота: Почивен ден']; back to portrait: kept 100% of 26 texts
PROCDEATH contacts: killed yes; new pid 15346; restored: kept 100% of 26 texts; front app
ROT documents: pid 15346→15346 (same); landscape: kept 75% of 16 texts; lost e.g. ['Устав', 'Отвори с приложение за PDF: Ус', 'Учредителни документи · 30.04.', 'Уставът представлява основният']; back to portrait: kept 100% of 16 texts
PROCDEATH documents: killed yes; new pid 15666; restored: kept 100% of 16 texts; front app
## RB2. Process death, back from Recents (article, viewer)
PD book before: book title kept; back button yes
PROCDEATH book (recents): killed yes; ; restored: book title kept; back button yes
## RB3. Totals
ROT not fully kept (<90%): ROT home 
PROCDEATH not fully restored (<90%): PROCDEATH home PROCDEATH diagnostics PROCDEATH search PROCDEATH book (recents) 
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 20 | Първи кадър: медиана 1591 мс, последен 1472 мс | Пълно показване: медиана 7294 мс, последно 10897 мс | Насечени кадри по екрани | more: 11,5 % от 148 кадъра · най-бавен 1243 мс | catalog: 8,7 % от 150 кадъра · най-бавен 1153 мс | album: 8,
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 20" text="Първи кадър: медиана 1591 мс, последен 1472 мс" text="Пълно показване: медиана 7294 мс, последно 10897 мс" text="Насечени кадри по екрани" text="more: 11,5 % от 148 кадъра · най-бавен 1243 мс" text=
APP W/E lines (pids ['5313', '7931', '8389', '8763', '9067', '9406', '9917', '10182', '10598', '10917', '11357', '11842', '12311', '12800', '13236', '13781', '14186', '14620', '15046', '15346', '15666', '15876']): 211 in 11 groups
      66  E/OpenGLRenderer: Unable to match the desired swap behavior.
      25  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      25  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      24  W/Parcel: Expecting binder but got null!
      22  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      22  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      22  W/Gralloc4: allocator #.x is not supported
       2  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#ce#-#eed-#b#-a#e-#e#d#d, generation=#)
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object g#.s(java.lang.Object)(r
       1  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at void z#.k()(r#-map-id-#a#f#e#bb#fa#d#ab#b
contact sheets: 10 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5313', '7931', '8389', '8763', '9067', '9406', '9917', '10182', '10598', '10917', '11357', '11842', '12311', '12800', '13236', '13781', '14186', '14620', '15046', '15346', '15666', '15876']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 69
   skipped-frames events=69 max=99 total=2959
== Davey: 176
   davey events=176 max=2958ms median=922ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## tour-a

```
part tour-a; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3717 ms
## T-bg-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP bg-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP bg-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Сканирай · Читателска карта
CAP bg-light/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра · БИБЛИОТЕКА · 21
CAP bg-light/01d-home-4: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя · Огънят на здрача · Дъхът на любовта · ВИТРИНА
CAP bg-light/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP bg-light/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP bg-light/02c-filters-2: targets=25 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=31 ellipsized=0 | Манипулатор за преместване с · графично издание · аудиодокумент · продължаващо издание · патент/стандарт · нотно издание
CAP bg-light/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP bg-light/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP bg-light/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP bg-light/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP bg-light/03-book: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP bg-light/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP bg-light/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP bg-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Забравеният мор · Още една · Към книгата · Затвори
CAP bg-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CAP bg-light/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP bg-light/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP bg-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP bg-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP bg-light/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP bg-light/08b-news-2: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=27 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP bg-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP bg-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP bg-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP bg-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP bg-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP bg-light/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP bg-light/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP bg-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP bg-light/12-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP bg-light/12b-more-2: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=22 ellipsized=0 | Съобщения и покани от читали · Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия
CAP bg-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP bg-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP bg-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP bg-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP bg-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP bg-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP bg-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP bg-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP bg-light/18-settings: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP bg-light/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български · Сезонни украси
CAP bg-light/18c-settings-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известия · Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два 
CAP bg-light/18d-settings-4: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Корици от интернет · Корици по ISBN от covers.ope · Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни · Новините, събитията, каталог
CAP bg-light/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP bg-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP bg-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP bg-light/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP bg-light/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP bg-light/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP bg-light/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP bg-light/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 2
CAP bg-light/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 2
CAP bg-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP bg-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP bg-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR bg-light: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=3 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=46
## T-bg-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night yes, app locale [bg-BG], rotation 0)
CAP bg-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP bg-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · СЪБИТИЕ · С хоро и песен във Водица · 26 септември 2026 · Моите книги · Читателска карта
CAP bg-dark/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НОЕ · Организацията на ООН за обра · БИБЛИОТЕКА · НОЕ · 155 г. от рождението на Пана · ЧИТАЛИЩЕ
CAP bg-dark/01d-home-4: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя · Огънят на здрача · Дъхът на любовта · ВИТРИНА
CAP bg-dark/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP bg-dark/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP bg-dark/02c-filters-2: targets=25 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=31 ellipsized=0 | Манипулатор за преместване с · графично издание · аудиодокумент · продължаващо издание · патент/стандарт · нотно издание
CAP bg-dark/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP bg-dark/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP bg-dark/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP bg-dark/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP bg-dark/03-book: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP bg-dark/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP bg-dark/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP bg-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Женските страхо · Още една · Към книгата · Затвори
CAP bg-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP bg-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP bg-dark/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP bg-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP bg-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP bg-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP bg-dark/08b-news-2: targets=21 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP bg-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP bg-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP bg-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP bg-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP bg-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP bg-dark/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP bg-dark/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP bg-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP bg-dark/12-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP bg-dark/12b-more-2: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=22 ellipsized=0 | Съобщения и покани от читали · Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия
CAP bg-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP bg-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP bg-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP bg-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP bg-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP bg-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP bg-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP bg-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP bg-dark/18-settings: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP bg-dark/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български · Сезонни украси
CAP bg-dark/18c-settings-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия
CAP bg-dark/18d-settings-4: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Корици по ISBN от covers.ope · Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни · Новините, събитията, каталог · Поверителност
CAP bg-dark/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP bg-dark/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP bg-dark/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP bg-dark/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP bg-dark/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP bg-dark/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP bg-dark/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP bg-dark/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 3
CAP bg-dark/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 3
CAP bg-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP bg-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP bg-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR bg-dark: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=4 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=46
## T-en-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night yes, app locale [en-US], rotation 0)
CAP en-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=1 [Search books, news, events…] | “Vasil Levski – 1922” · Yavorets village · Messages · Search · Search books, news, events… · Scan barcode
CAP en-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Latest news · Четем заедно – мъдростта на  · November 11, 2026 · My books · Library card · Want to read
CAP en-dark/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | 16 · NOV · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра · БИБЛИОТЕКА · 21
CAP en-dark/01d-home-4: targets=16 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | POPULAR AND NEW · For you · Пътят на пътя · Огънят на здрача · Дъхът на любовта · SHOWCASE
CAP en-dark/01e-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Search · Back · Clear · All · Books
CAP en-dark/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Search everything · Scan barcode · Everything · Title · Author · ISBN
CAP en-dark/02b-filters: targets=26 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=30 ellipsized=0 | Drag handle · Filters · Reset filters · Available only · Document type · книга
CAP en-dark/02c-filters-2: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=0 | Drag handle · патент/стандарт · нотно издание · Section (genre) · General works · Philosophy. Psychology
CAP en-dark/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Best match  ✓ · Title (A–Z) · Author (A–Z) · Year (newest first) · Year (oldest first) · Recently added
CAP en-dark/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Search everything · Scan barcode · Everything · Title · Author · ISBN
CAP en-dark/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Search everything · Scan barcode · Everything · Title · Author · ISBN
CAP en-dark/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Clear · Everything · Title · Author · ISBN
CAP en-dark/03-book: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Not on the shelf · Availability as of Nov 9, 20 · Want to read · Ask the library
CAP en-dark/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Availability as of Nov 9, 20 · Want to read · Ask the library · Notify me when it’s on the s · Bibliographic details · Publisher
CAP en-dark/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Availability as of Nov 9, 20 · Want to read · Ask the library · Notify me when it’s on the s · Bibliographic details · Publisher
CAP en-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Suggestion: Златният спомени · Another one · Open the book · Close
CAP en-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Welcome! · News, events, the catalogue  · Sign in · LIBRARY · Library card · Card you entered
CAP en-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Welcome! · News, events, the catalogue  · Sign in · LIBRARY · Library card · Card you entered
CAP en-dark/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | LIBRARY CARD · “Vasil Levski – 1922” · — · Reader no. TEST-12345 · Barcode for card TEST-12345 · Show card
CAP en-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Close · LIBRARY CARD · “Vasil Levski – 1922” · — · Reader no. TEST-12345 · Barcode for card TEST-12345
CAP en-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Your list is empty · Mark a book with “Want to re · Back · Want to read
CAP en-dark/08-news: targets=16 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | News · Calendar · Search news · All · Saved · Библиотека
CAP en-dark/08b-news-2: targets=23 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=5 | front: app | texts=28 ellipsized=0 | News · Calendar · Search news · All · Saved · Библиотека
CAP en-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  July 25, 2026 · Групата за народни танци при · Групата за народни танци при · Reading mode · Aa
CAP en-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  July 25, 2026 · Групата за народни танци при · Групата за народни танци при · Reading mode · Aa
CAP en-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Close sheet · Drag handle · Reading mode · Font size · Aa · Small text
CAP en-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [A card with a QR code — for In] | Close sheet · Drag handle · Share · Share as an image · A card with a QR code — for  · Share the link
CAP en-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | News · Calendar · Subscribe to the calendar · Calendar view · All · Библиотека
CAP en-dark/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · November 16, 2026 · An event from the centre’s a · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP en-dark/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · November 16, 2026 · An event from the centre’s a · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP en-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [A card with a QR code — for In] | Close sheet · Drag handle · Share · Share as an image · A card with a QR code — for  · Share the link
CAP en-dark/12-more: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | THE CENTRE · Messages · Announcements and invitation · Events · Community centre calendar · Activities
CAP en-dark/12b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=23 ellipsized=0 | Announcements and invitation · Community centre calendar · Activities · Groups, clubs, projects · About us · History and mission
CAP en-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 photos · Фотодокументална изложба · 4 photos · Фолклорна група „Явор“ · 6 photos · Клуб за народни танци
CAP en-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP en-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Close · 1 / 29  ·  Сградата на читал · Share · Open on the website · About this photo
CAP en-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Close · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Share · Open on the website
CAP en-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Email the centre · Email · Show the centre on the map · Map · Open the centre’s website
CAP en-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Address · пл. 9-ти Септември 3, с. Яво · Contact persons · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Email
CAP en-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Documents · Historical articles · Декларации · May 14, 2026 ·  · Декларация за частни интерес · Декларацията за частни интер · Open with a PDF app: Деклара
CAP en-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Documents · Historical articles · Декларации · May 14, 2026 ·  · Декларация за частни интерес · Декларацията за частни интер · Open with a PDF app: Деклара
CAP en-dark/18-settings: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Appearance · Theme · System default · Text size · Standard · Palette
CAP en-dark/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Black (OLED) · Pure black background in the · Colours from wallpaper · Material You colours from yo · Language · English
CAP en-dark/18c-settings-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Shake the phone in the catal · Notifications · Choose notifications · Loans, events, messages and  · Updates · Automatic updates
CAP en-dark/18d-settings-4: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Covers from the internet · Covers by ISBN from covers.o · Covers on Wi-Fi only · On mobile data, only covers  · Clear temporary data · News, events, catalogue, cov
CAP en-dark/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Text size · Multiplies the font size set · Small · Standard · Large · Extra large
CAP en-dark/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Language · System default · Български · English · Close
CAP en-dark/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Theme · System default · Light · Dark · Close
CAP en-dark/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Notifications are prepared o · Messages · Messages the centre sends to · News · A new post on the website · Events
CAP en-dark/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | See also: Политика за повери · Last updated: November 10, 2 · Who processes the data · “Chitalishte Yavorets” is th · Principle · The app collects as little d
CAP en-dark/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Purpose · The app provides information · Content · News, events, photos and tex · Book availability · Catalogue availability is in
CAP en-dark/18k-about: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Chitalishte Yavorets · Community Centre “Vasil Levs · Version 1.2.35 (1507364) · The official mobile app of t · Where the data comes from · News, events, photos, docume
CAP en-dark/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | The app records technical er · Share · Clear · Performance · Start-up times and screen sm · Cold start · 4
CAP en-dark/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | The app records technical er · Share · Clear · Performance · Start-up times and screen sm · Cold start · 4
CAP en-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Reader sign-in · Reader number · PIN (from the library) · Show PIN · Keep me signed in on this de · Sign in
CAP en-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Enter the number manually · The barcode scanner comes wi · The ISBN (10 or 13 digits un · ISBN or inventory number · Cancel · Search
CAP en-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Enter the number manually · The barcode scanner comes wi · The ISBN (10 or 13 digits un · ISBN or inventory number · Cancel · Search
TOUR en-dark: A11Y AGG: 57 screens analysed, 0 dumps failed; totals MISSING=8 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=48
## T-en-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [en-US], rotation 0)
CAP en-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=1 [Search books, news, events…] | “Vasil Levski – 1922” · Yavorets village · Messages · Search · Search books, news, events… · Scan barcode
CAP en-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Latest news · С хоро и песен във Водица · September 26, 2026 · My books · Library card · Want to read
CAP en-light/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | 16 · NOV · Организацията на ООН за обра · БИБЛИОТЕКА · 21 · NOV
CAP en-light/01d-home-4: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | POPULAR AND NEW · For you · Пътят на пътя · Огънят на здрача · Дъхът на любовта · SHOWCASE
CAP en-light/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Search everything · Scan barcode · Everything · Title · Author · ISBN
CAP en-light/02b-filters: targets=26 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=30 ellipsized=0 | Drag handle · Filters · Reset filters · Available only · Document type · книга
CAP en-light/02c-filters-2: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=0 | Drag handle · патент/стандарт · нотно издание · Section (genre) · General works · Philosophy. Psychology
CAP en-light/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Best match  ✓ · Title (A–Z) · Author (A–Z) · Year (newest first) · Year (oldest first) · Recently added
CAP en-light/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Search everything · Scan barcode · Everything · Title · Author · ISBN
CAP en-light/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Search everything · Scan barcode · Everything · Title · Author · ISBN
CAP en-light/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Clear · Everything · Title · Author · ISBN
CAP en-light/03-book: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Not on the shelf · Availability as of Nov 9, 20 · Want to read · Ask the library
CAP en-light/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Availability as of Nov 9, 20 · Want to read · Ask the library · Notify me when it’s on the s · Bibliographic details · Publisher
CAP en-light/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Availability as of Nov 9, 20 · Want to read · Ask the library · Notify me when it’s on the s · Bibliographic details · Publisher
CAP en-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Suggestion: Огънят на детств · Another one · Open the book · Close
CAP en-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Welcome! · News, events, the catalogue  · Sign in · LIBRARY · Library card · Card you entered
CAP en-light/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Welcome! · News, events, the catalogue  · Sign in · LIBRARY · Library card · Card you entered
CAP en-light/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | LIBRARY CARD · “Vasil Levski – 1922” · — · Reader no. TEST-12345 · Barcode for card TEST-12345 · Show card
CAP en-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Close · LIBRARY CARD · “Vasil Levski – 1922” · — · Reader no. TEST-12345 · Barcode for card TEST-12345
CAP en-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Your list is empty · Mark a book with “Want to re · Back · Want to read
CAP en-light/08-news: targets=16 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | News · Calendar · Search news · All · Saved · Библиотека
CAP en-light/08b-news-2: targets=21 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=5 | front: app | texts=26 ellipsized=0 | News · Calendar · Search news · All · Saved · Библиотека
CAP en-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  July 25, 2026 · Групата за народни танци при · Групата за народни танци при · Reading mode · Aa
CAP en-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  July 25, 2026 · Групата за народни танци при · Групата за народни танци при · Reading mode · Aa
CAP en-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Close sheet · Drag handle · Reading mode · Font size · Aa · Small text
CAP en-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [A card with a QR code — for In] | Close sheet · Drag handle · Share · Share as an image · A card with a QR code — for  · Share the link
CAP en-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | News · Calendar · Subscribe to the calendar · Calendar view · All · Библиотека
CAP en-light/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · November 16, 2026 · An event from the centre’s a · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP en-light/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · November 16, 2026 · An event from the centre’s a · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP en-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [A card with a QR code — for In] | Close sheet · Drag handle · Share · Share as an image · A card with a QR code — for  · Share the link
CAP en-light/12-more: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | THE CENTRE · Messages · Announcements and invitation · Events · Community centre calendar · Activities
CAP en-light/12b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=23 ellipsized=0 | Announcements and invitation · Community centre calendar · Activities · Groups, clubs, projects · About us · History and mission
CAP en-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 photos · Фотодокументална изложба · 4 photos · Фолклорна група „Явор“ · 6 photos · Клуб за народни танци
CAP en-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP en-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Close · 1 / 29  ·  Сградата на читал · Share · Open on the website · About this photo
CAP en-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Close · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Share · Open on the website
CAP en-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Email the centre · Email · Show the centre on the map · Map · Open the centre’s website
CAP en-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Address · пл. 9-ти Септември 3, с. Яво · Contact persons · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Email
CAP en-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Documents · Historical articles · Декларации · May 14, 2026 ·  · Декларация за частни интерес · Декларацията за частни интер · Open with a PDF app: Деклара
CAP en-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Documents · Historical articles · Декларации · May 14, 2026 ·  · Декларация за частни интерес · Декларацията за частни интер · Open with a PDF app: Деклара
CAP en-light/18-settings: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Appearance · Theme · System default · Text size · Standard · Palette
CAP en-light/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Pure black background in the · Colours from wallpaper · Material You colours from yo · Language · English · Seasonal decorations
CAP en-light/18c-settings-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Choose notifications · Loans, events, messages and  · Updates · Automatic updates · Checked on launch and twice  · Check for updates
CAP en-light/18d-settings-4: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Covers by ISBN from covers.o · Covers on Wi-Fi only · On mobile data, only covers  · Clear temporary data · News, events, catalogue, cov · Privacy
CAP en-light/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Text size · Multiplies the font size set · Small · Standard · Large · Extra large
CAP en-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Language · System default · Български · English · Close
CAP en-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Theme · System default · Light · Dark · Close
CAP en-light/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Notifications are prepared o · Messages · Messages the centre sends to · News · A new post on the website · Events
CAP en-light/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | See also: Политика за повери · Last updated: November 10, 2 · Who processes the data · “Chitalishte Yavorets” is th · Principle · The app collects as little d
CAP en-light/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Purpose · The app provides information · Content · News, events, photos and tex · Book availability · Catalogue availability is in
CAP en-light/18k-about: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Chitalishte Yavorets · Community Centre “Vasil Levs · Version 1.2.35 (1507364) · The official mobile app of t · Where the data comes from · News, events, photos, docume
CAP en-light/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | The app records technical er · Share · Clear · Performance · Start-up times and screen sm · Cold start · 5
CAP en-light/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | The app records technical er · Share · Clear · Performance · Start-up times and screen sm · Cold start · 5
CAP en-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Reader sign-in · Reader number · PIN (from the library) · Show PIN · Keep me signed in on this de · Sign in
CAP en-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Enter the number manually · The barcode scanner comes wi · The ISBN (10 or 13 digits un · ISBN or inventory number · Cancel · Search
CAP en-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Enter the number manually · The barcode scanner comes wi · The ISBN (10 or 13 digits un · ISBN or inventory number · Cancel · Search
TOUR en-light: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=5 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=48
## T. Accessibility over all screens of this part
A11Y AGG: 225 screens analysed, 0 dumps failed; totals MISSING=20 DOUBLE=0 SAME-LABEL=20 SMALL(<48dp)=188
== MISSING: 10 distinct
   View@[0,2035][1080,2127]  [1 screens: 08b-news-2]
   View@[556,296][1038,349]  [1 screens: 12b-more-2]
   View@[42,2321][524,2400]  [1 screens: 13-gallery]
   View@[556,2321][1038,2400]  [1 screens: 13-gallery]
   View@[42,0][378,5]  [1 screens: 01d-home-4]
   View@[415,0][751,5]  [1 screens: 01d-home-4]
   View@[788,0][1080,5]  [1 screens: 01d-home-4]
   View@[1055,616][1080,742]  [1 screens: 08-news]
   View@[1055,448][1080,511]  [1 screens: 08b-news-2]
   View@[0,2043][1080,2127]  [1 screens: 12-more]
== SMALL: 32 distinct
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   'Drag handle'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   'Бевълстоук : Кн. 1 - 2, Куин, '  [2 screens: 02e-shelf, 02f-shelf-2]
   'Безсрамна добродетел, Робъртс,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият дома, Николай Симеонов,'  [2 screens: 02e-shelf, 02f-shelf-2]
   '12 -те вятъра, Гарфилд, Джудит'  [1 screens: 02e-shelf]
   'Аз обичам Ню Йорк, Келк, Линдз'  [1 screens: 02e-shelf]
   'Ако върнеш времето назад, Потъ'  [1 screens: 02e-shelf]
   'Алтъна, Никова, Ивелина'  [1 screens: 02e-shelf]
   'Анатомът, Андахази, Федерико'  [1 screens: 02e-shelf]
   'Белият звездите, Диана Христов'  [1 screens: 02e-shelf]
   'Всичко'  [1 screens: 02f-shelf-2]
   'Заглавие'  [1 screens: 02f-shelf-2]
   'Автор'  [1 screens: 02f-shelf-2]
   'ISBN'  [1 screens: 02f-shelf-2]
   'Белият вятъра, Петър Здравков'  [1 screens: 02f-shelf-2]
   'Белият детството, Симеон Колев'  [1 screens: 02f-shelf-2]
   'Всички'  [1 screens: 08b-news-2]
   'Запазени'  [1 screens: 08b-news-2]
   'Библиотека'  [1 screens: 08b-news-2]
   'Съобщения и покани от читалище'  [1 screens: 12b-more-2]
   ''  [1 screens: 12b-more-2]
   'Everything'  [1 screens: 02f-shelf-2]
   'Title'  [1 screens: 02f-shelf-2]
   'Author'  [1 screens: 02f-shelf-2]
== SAME-LABEL: 2 distinct
   'Манипулатор за преместване с п' x2  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   'Drag handle' x2  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
== DOUBLE: 0 distinct
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 5 | Първи кадър: медиана 2272 мс, последен 2680 мс | Пълно показване: медиана 2600 мс, последно 3102 мс | Топъл старт · 1 | Първи кадър: медиана 646 мс, последен 646 мс | Насечени кадри по екрани | article: 11,3 % от 115 кадъра · най-бавен 527 
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 5" text="Първи кадър: медиана 2272 мс, последен 2680 мс" text="Пълно показване: медиана 2600 мс, последно 3102 мс" text="Топъл старт · 1" text="Първи кадър: медиана 646 мс, последен 646 мс" text="Насечени кад
APP W/E lines (pids ['5609', '6715', '12451', '17563', '22413']): 664 in 89 groups
      41  E/OpenGLRenderer: Unable to match the desired swap behavior.
      40  W/Parcel: Expecting binder but got null!
      14  W/BasicSecureTextField: 	at o#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
      12  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
      12  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       9  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       9  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       8  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       8  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       8  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       8  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       8  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       8  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       8  W/BasicSecureTextField: 	at h#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at vy#.b(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at vy#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at jr#.invoke(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at sn#.c(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at sn#.invoke(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at g#.b(Unknown Source:#)
       8  W/BasicSecureTextField: 	at kr#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at a#.f(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at sn#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at sn#.f(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at d#.invoke(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at al#.x(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at u#.o(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at gq#.j(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at dc#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
contact sheets: 25 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5609', '6715', '12451', '17563', '22413']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 23
   skipped-frames events=23 max=104 total=1013
== Davey: 57
   davey events=57 max=2626ms median=847ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 2 block(s) {'FATAL': 2} → crashes.txt
```

## tour-b

```
part tour-b; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 5423 ms
TEXT SIZE now: text="Много голям" 
## T-font130-xlarge. All screens (1080x2400 px, density 420, font_scale 1.3, night no, app locale [bg-BG], rotation 0)
CAP font130-xlarge/01-home: targets=12 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP font130-xlarge/01b-home-2: targets=12 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Сканирай · Читателска карта
CAP font130-xlarge/01c-home-3: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | Сканирай · Читателска карта · Искам да прочета · Контакти · КАЛЕНДАР · Предстоящи събития
CAP font130-xlarge/01d-home-4: targets=13 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра · БИБЛИОТЕКА · БИБЛИОТЕКА
CAP font130-xlarge/01e-search-12: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=37 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP font130-xlarge/02-catalog: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=41 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · 2446 резултата  ·  данни от 
CAP font130-xlarge/02b-filters: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP font130-xlarge/02c-filters-2: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=2 | front: app | texts=20 ellipsized=0 | Манипулатор за преместване с · аудиодокумент · продължаващо издание · патент/стандарт · нотно издание · Раздел (жанр)
CAP font130-xlarge/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP font130-xlarge/02e-shelf: targets=46 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · 2446 резултата  ·  данни от 
CAP font130-xlarge/02f-shelf-2: targets=43 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=20 | front: app | texts=45 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · Бевълстоук : Кн. 1 - 2, Куин
CAP font130-xlarge/02g-search-12: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=37 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · 12 -те вятъра
CAP font130-xlarge/03-book: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP font130-xlarge/03b-book-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство · Сиела, Русе · Година
CAP font130-xlarge/03c-book-3: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Раздел · Език. Литература (УДК 82) · Ключови думи · изкуство · Сигнатура · 82-П24
CAP font130-xlarge/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Дъхът на планин · Още една · Към книгата · Затвори
CAP font130-xlarge/05-my: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CAP font130-xlarge/05b-my-2: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт · Моите книги · Заети книги и срокове за връ · Искам да прочета
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP font130-xlarge/06-card: targets=7 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=13 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP font130-xlarge/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP font130-xlarge/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP font130-xlarge/08-news: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP font130-xlarge/08b-news-2: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=17 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP font130-xlarge/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP font130-xlarge/09b-article-2: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=3 ellipsized=0 | Групата за народни танци при · Групата за народни танци при · Назад
CAP font130-xlarge/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP font130-xlarge/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP font130-xlarge/10-calendar: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=28 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP font130-xlarge/11-event: targets=6 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Панайот Пипков · Ден на семе · 21 ноември 2026 · Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана
CAP font130-xlarge/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара · Маршрут
CAP font130-xlarge/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP font130-xlarge/12-more: targets=12 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP font130-xlarge/12b-more-2: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | За читалището · История и мисия · Галерия · Снимки от сайта · Документи и архив · Устав, отчети, юбилейният ве
CAP font130-xlarge/13-gallery: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP font130-xlarge/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP font130-xlarge/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP font130-xlarge/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP font130-xlarge/16-contacts: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP font130-xlarge/16b-contacts-2: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл · chitalishte_yavorec@abv.bg
CAP font130-xlarge/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP font130-xlarge/17b-documents-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Документи · Исторически публикации · Декларация за частни интерес · Декларацията за частни интер · Годишни отчети · 30.04.2026  · Отчет за дейност 2025
CAP font130-xlarge/18-settings: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Много голям · Палитра
CAP font130-xlarge/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Злато и жито · Зелено и сребро · Червено, черно и слонова кос · Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета
CAP font130-xlarge/18c-settings-3: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Цветовете на телефона, взети · Език · Български · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP font130-xlarge/18d-settings-4: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Разклащане за „Изненадай ме“ · Разклати телефона в каталога · Известия · Избери известията · Заемания, събития, съобщения · Обновления
CAP font130-xlarge/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP font130-xlarge/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP font130-xlarge/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP font130-xlarge/18h-notifications: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
WARN: settings item =Политика за поверителност|=Privacy policy not found
CAP font130-xlarge/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Назад
CAP font130-xlarge/18k-about: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP font130-xlarge/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 2
CAP font130-xlarge/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=14 ellipsized=0 | Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 2 · Първи кадър: медиана 3572 мс
CAP font130-xlarge/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP font130-xlarge/20-scanner: targets=3 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Назад · Сканиране
CAP font130-xlarge/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR font130-xlarge: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=9 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=47
## T-font130-xlarge-en. All screens (1080x2400 px, density 420, font_scale 1.3, night no, app locale [en-US], rotation 0)
CAP font130-xlarge-en/01-home: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=1 [Search books, news, events…] | “Vasil Levski – 1922” · Yavorets village · Messages · Search · Search books, news, events… · Scan barcode
CAP font130-xlarge-en/01b-home-2: targets=13 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=24 ellipsized=0 | Latest news · БИБЛИОТЕКА · Четем заедно – мъдростта на  · November 11, 2026 · My books · Library card
CAP font130-xlarge-en/01c-home-3: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | My books · Library card · Want to read · Contacts · CALENDAR · Upcoming events
CAP font130-xlarge-en/01d-home-4: targets=16 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=23 ellipsized=0 | Организацията на ООН за обра · БИБЛИОТЕКА · LIBRARY · New arrivals · Open catalogue · Дъхът на спомените — том 3
CAP font130-xlarge-en/01e-search-12: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=37 ellipsized=0 | 12 · Search · Back · Clear · All · Books
CAP font130-xlarge-en/02-catalog: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=42 ellipsized=0 | Search everything · Scan barcode · Everything · Title · Author · ISBN
CAP font130-xlarge-en/02b-filters: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=21 ellipsized=0 | Drag handle · Filters · Reset filters · Available only · Document type · книга
CAP font130-xlarge-en/02c-filters-2: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=3 | front: app | texts=21 ellipsized=0 | Drag handle · аудиодокумент · продължаващо издание · патент/стандарт · нотно издание · Section (genre)
CAP font130-xlarge-en/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Best match  ✓ · Title (A–Z) · Author (A–Z) · Year (newest first) · Year (oldest first) · Recently added
CAP font130-xlarge-en/02e-shelf: targets=47 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=50 ellipsized=0 | Search everything · Scan barcode · Everything · Title · Author · ISBN
CAP font130-xlarge-en/02f-shelf-2: targets=44 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=18 | front: app | texts=46 ellipsized=0 | Search everything · Scan barcode · Everything · Title · Author · ISBN
CAP font130-xlarge-en/02g-search-12: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | 12 · Clear · Everything · Title · Author · ISBN
CAP font130-xlarge-en/03-book: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Спомен за здрача · Веска Пенчева · Not on the shelf · Availability as of Nov 9, 20 · Want to read · Ask the library
CAP font130-xlarge-en/03b-book-2: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Bibliographic details · Publisher · Сиела, Русе · Year · 1972 · ISBN
CAP font130-xlarge-en/03c-book-3: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | ISBN · 9789546551135 · Language · български · Type · графично издание
CAP font130-xlarge-en/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Suggestion: Да изгубиш Хоуп  · Another one · Open the book · Close
CAP font130-xlarge-en/05-my: targets=10 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=17 ellipsized=0 | Welcome! · News, events, the catalogue  · Sign in · LIBRARY · Library card · Card you entered
CAP font130-xlarge-en/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Sign in · LIBRARY · Library card · Card you entered · My books · Borrowed books and due dates
CAP font130-xlarge-en/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | LIBRARY CARD · “Vasil Levski – 1922” · — · Reader no. TEST-12345 · Barcode for card TEST-12345 · Show card
CAP font130-xlarge-en/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Close · LIBRARY CARD · “Vasil Levski – 1922” · — · Reader no. TEST-12345 · Barcode for card TEST-12345
CAP font130-xlarge-en/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Your list is empty · Mark a book with “Want to re · Back · Want to read
CAP font130-xlarge-en/08-news: targets=13 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | News · Calendar · Search news · All · Saved · Библиотека
CAP font130-xlarge-en/08b-news-2: targets=18 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=4 | front: app | texts=22 ellipsized=0 | News · Calendar · Search news · All · Saved · Библиотека
CAP font130-xlarge-en/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  July 25, 2026 · Групата за народни танци при · Групата за народни танци при · Reading mode · Aa
CAP font130-xlarge-en/09b-article-2: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=3 ellipsized=0 | Групата за народни танци при · Групата за народни танци при · Back
CAP font130-xlarge-en/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Close sheet · Drag handle · Reading mode · Font size · Aa · Small text
CAP font130-xlarge-en/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [A card with a QR code — for In] | Close sheet · Drag handle · Share · Share as an image · A card with a QR code — for  · Share the link
CAP font130-xlarge-en/10-calendar: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=28 ellipsized=0 | News · Calendar · Subscribe to the calendar · Calendar view · All · Библиотека
CAP font130-xlarge-en/11-event: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Панайот Пипков · Ден на семе · November 21, 2026 · An event from the centre’s a · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана
CAP font130-xlarge-en/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | November 21, 2026 · An event from the centre’s a · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Add to calendar
CAP font130-xlarge-en/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [A card with a QR code — for In] | Close sheet · Drag handle · Share · Share as an image · A card with a QR code — for  · Share the link
CAP font130-xlarge-en/12-more: targets=12 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | THE CENTRE · Messages · Announcements and invitation · Events · Community centre calendar · Activities
CAP font130-xlarge-en/12b-more-2: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Groups, clubs, projects · About us · History and mission · Gallery · Photos from the website · Documents & archive
CAP font130-xlarge-en/13-gallery: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 photos · Фотодокументална изложба · 4 photos · Фолклорна група „Явор“ · 6 photos · Клуб за народни танци
CAP font130-xlarge-en/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP font130-xlarge-en/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Close · 1 / 29  ·  Сградата на читал · Share · Open on the website · About this photo
CAP font130-xlarge-en/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Close · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Share · Open on the website
CAP font130-xlarge-en/16-contacts: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | НЧ „Васил Левски – 1922“ · Email the centre · Email · Show the centre on the map · Map · Open the centre’s website
CAP font130-xlarge-en/16b-contacts-2: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | пл. 9-ти Септември 3, с. Яво · Contact persons · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Email · chitalishte_yavorec@abv.bg
CAP font130-xlarge-en/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Documents · Historical articles · Декларации · May 14, 2026 ·  · Декларация за частни интерес · Декларацията за частни интер · Open with a PDF app: Деклара
CAP font130-xlarge-en/17b-documents-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Documents · Historical articles · Декларация за частни интерес · Декларацията за частни интер · Годишни отчети · Apr 30, 202 · Отчет за дейност 2025
CAP font130-xlarge-en/18-settings: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Appearance · Theme · System default · Text size · Extra large · Palette
CAP font130-xlarge-en/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Gold and wheat · Green and silver · Red, black and ivory · Black (OLED) · Pure black background in the · Colours from wallpaper
CAP font130-xlarge-en/18c-settings-3: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Material You colours from yo · Language · English · Seasonal decorations · Holidays, seasons and tradit · Shake for “Surprise me”
CAP font130-xlarge-en/18d-settings-4: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Shake the phone in the catal · Notifications · Choose notifications · Loans, events, messages and  · Updates · Automatic updates
CAP font130-xlarge-en/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Text size · Multiplies the font size set · Small · Standard · Large · Extra large
CAP font130-xlarge-en/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Language · System default · Български · English · Close
CAP font130-xlarge-en/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Theme · System default · Light · Dark · Close
CAP font130-xlarge-en/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Notifications are prepared o · Messages · Messages the centre sends to · News · A new post on the website · Events
CAP font130-xlarge-en/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | See also: Политика за повери · Last updated: November 10, 2 · Who processes the data · “Chitalishte Yavorets” is th · Principle · The app collects as little d
CAP font130-xlarge-en/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Purpose · The app provides information · Content · News, events, photos and tex · Book availability · Catalogue availability is in
CAP font130-xlarge-en/18k-about: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Chitalishte Yavorets · Community Centre “Vasil Levs · Version 1.2.35 (1507364) · The official mobile app of t · Where the data comes from · News, events, photos, docume
CAP font130-xlarge-en/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | The app records technical er · Share · Clear · Performance · Start-up times and screen sm · Cold start · 3
CAP font130-xlarge-en/18m-diagnostics-2: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Start-up times and screen sm · Cold start · 3 · First frame: median 3870 ms, · Fully drawn: median 5192 ms, · Warm start · 2 · First frame: median 810 ms, 
CAP font130-xlarge-en/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Reader sign-in · Reader number · PIN (from the library) · Show PIN · Keep me signed in on this de · Sign in
CAP font130-xlarge-en/20-scanner: targets=3 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Enter the number manually · The barcode scanner comes wi · The ISBN (10 or 13 digits un · ISBN or inventory number · Back · Scan
CAP font130-xlarge-en/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Enter the number manually · The barcode scanner comes wi · The ISBN (10 or 13 digits un · ISBN or inventory number · Cancel · Search
TOUR font130-xlarge-en: A11Y AGG: 57 screens analysed, 0 dumps failed; totals MISSING=8 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=50
TEXT SIZE now: text="Стандартен" 
## T-landscape-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP landscape-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP landscape-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | 12 ноември 2026 · Св. Йоан М · Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Моите книги
CAP landscape-light/01c-home-3: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Моите книги · Читателска карта · Искам да прочета · Контакти · КАЛЕНДАР · Предстоящи събития
CAP landscape-light/01d-home-4: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НОЕ · Организацията на ООН за обра · БИБЛИОТЕКА · НОЕ · 155 г. от рождението на Пана · ЧИТАЛИЩЕ
CAP landscape-light/01e-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP landscape-light/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP landscape-light/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP landscape-light/02c-filters-2: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=3 | front: app | texts=29 ellipsized=0 | Манипулатор за преместване с · патент/стандарт · нотно издание · Раздел (жанр) · Общ отдел · Философия. Психология
CAP landscape-light/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP landscape-light/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP landscape-light/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP landscape-light/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP landscape-light/03-book: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP landscape-light/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP landscape-light/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP landscape-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Далечният селот · Още една · Към книгата · Затвори
CAP landscape-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP landscape-light/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP landscape-light/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP landscape-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP landscape-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP landscape-light/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP landscape-light/08b-news-2: targets=21 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP landscape-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP landscape-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP landscape-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP landscape-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP landscape-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP landscape-light/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP landscape-light/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP landscape-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP landscape-light/12-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP landscape-light/12b-more-2: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=22 ellipsized=0 | Съобщения и покани от читали · Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия
CAP landscape-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP landscape-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP landscape-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP landscape-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP landscape-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP landscape-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP landscape-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP landscape-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP landscape-light/18-settings: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP landscape-light/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български · Сезонни украси
CAP landscape-light/18c-settings-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Известия · Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два 
CAP landscape-light/18d-settings-4: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Данни · Корици от интернет · Корици по ISBN от covers.ope · Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни
CAP landscape-light/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP landscape-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP landscape-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP landscape-light/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP landscape-light/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP landscape-light/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP landscape-light/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP landscape-light/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 4
CAP landscape-light/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 4
CAP landscape-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP landscape-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP landscape-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR landscape-light: A11Y AGG: 57 screens analysed, 0 dumps failed; totals MISSING=4 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=48
## T-landscape-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night yes, app locale [bg-BG], rotation 0)
CAP landscape-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP landscape-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | 12 ноември 2026 · Св. Йоан М · Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Моите книги
CAP landscape-dark/01c-home-3: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Моите книги · Читателска карта · Искам да прочета · Контакти · КАЛЕНДАР · Предстоящи събития
CAP landscape-dark/01d-home-4: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP landscape-dark/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP landscape-dark/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP landscape-dark/02c-filters-2: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | Манипулатор за преместване с · Раздел (жанр) · Общ отдел · Философия. Психология · Религия · Обществени науки
CAP landscape-dark/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP landscape-dark/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP landscape-dark/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP landscape-dark/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP landscape-dark/03-book: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP landscape-dark/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP landscape-dark/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP landscape-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Гласът на звезд · Още една · Към книгата · Затвори
CAP landscape-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP landscape-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP landscape-dark/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP landscape-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP landscape-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP landscape-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP landscape-dark/08b-news-2: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=4 | front: app | texts=27 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP landscape-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP landscape-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP landscape-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP landscape-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP landscape-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP landscape-dark/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP landscape-dark/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP landscape-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP landscape-dark/12-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP landscape-dark/12b-more-2: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=22 ellipsized=0 | Съобщения и покани от читали · Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия
CAP landscape-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP landscape-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP landscape-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP landscape-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP landscape-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP landscape-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP landscape-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP landscape-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP landscape-dark/18-settings: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP landscape-dark/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български · Сезонни украси
CAP landscape-dark/18c-settings-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия
CAP landscape-dark/18d-settings-4: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Корици по ISBN от covers.ope · Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни · Новините, събитията, каталог · Поверителност
CAP landscape-dark/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP landscape-dark/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP landscape-dark/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP landscape-dark/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP landscape-dark/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP landscape-dark/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP landscape-dark/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP landscape-dark/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 5
CAP landscape-dark/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 5
CAP landscape-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP landscape-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP landscape-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR landscape-dark: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=3 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=47
## T. Accessibility over all screens of this part
A11Y AGG: 226 screens analysed, 0 dumps failed; totals MISSING=24 DOUBLE=0 SAME-LABEL=20 SMALL(<48dp)=192
== MISSING: 20 distinct
   View@[1030,1529][1080,1975]  [1 screens: 01b-home-2]
   View@[1030,698][1080,1144]  [1 screens: 01c-home-3]
   View@[1030,0][1080,138]  [1 screens: 01d-home-4]
   View@[42,2039][1038,2127]  [1 screens: 05-my]
   View@[1051,653][1080,779]  [1 screens: 08-news]
   View@[1051,485][1080,548]  [1 screens: 08b-news-2]
   View@[42,2028][1038,2127]  [1 screens: 12-more]
   View@[381,1454][658,1580]  [1 screens: 20-scanner]
   View@[42,1974][540,2127]  [1 screens: 01-home]
   View@[540,1974][1038,2127]  [1 screens: 01-home]
   View@[1030,1560][1080,2096]  [1 screens: 01b-home-2]
   View@[1030,827][1080,1363]  [1 screens: 01c-home-3]
   View@[1030,0][1080,395]  [1 screens: 01d-home-4]
   View@[891,2317][1017,2392]  [1 screens: 06-card]
   View@[53,2322][1027,2400]  [1 screens: 11-event]
   View@[399,1454][673,1580]  [1 screens: 20-scanner]
   View@[556,296][1038,349]  [1 screens: 12b-more-2]
   View@[42,2321][524,2400]  [1 screens: 13-gallery]
   View@[556,2321][1038,2400]  [1 screens: 13-gallery]
   View@[0,2047][1080,2127]  [1 screens: 08b-news-2]
== SMALL: 39 distinct
   'Drag handle'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   ''  [4 screens: 05-my, 06-card, 12-more, 12b-more-2]
   'Бевълстоук : Кн. 1 - 2, Куин, '  [2 screens: 02e-shelf, 02f-shelf-2]
   'Безсрамна добродетел, Робъртс,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият дома, Николай Симеонов,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Open catalogue'  [1 screens: 01b-home-2]
   'Далечният морето'  [1 screens: 01d-home-4]
   'Дългият пътя'  [1 screens: 01d-home-4]
   'аудиодокумент'  [1 screens: 02c-filters-2]
   '12 -те вятъра, Гарфилд, Джудит'  [1 screens: 02e-shelf]
   'Аз обичам Ню Йорк, Келк, Линдз'  [1 screens: 02e-shelf]
   'Ако върнеш времето назад, Потъ'  [1 screens: 02e-shelf]
   'Алтъна, Никова, Ивелина'  [1 screens: 02e-shelf]
   'Анатомът, Андахази, Федерико'  [1 screens: 02e-shelf]
   'Белият звездите, Диана Христов'  [1 screens: 02e-shelf]
   'Everything'  [1 screens: 02f-shelf-2]
   'Title'  [1 screens: 02f-shelf-2]
   'Author'  [1 screens: 02f-shelf-2]
   'Белият вятъра, Петър Здравков'  [1 screens: 02f-shelf-2]
   'Белият детството, Симеон Колев'  [1 screens: 02f-shelf-2]
   'All'  [1 screens: 08b-news-2]
   'Saved'  [1 screens: 08b-news-2]
   'Библиотека'  [1 screens: 08b-news-2]
   'Save: Празника „38 читалища. Е'  [1 screens: 08b-news-2]
== SAME-LABEL: 2 distinct
   'Drag handle' x2  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   'Манипулатор за преместване с п' x2  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
== DOUBLE: 0 distinct
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 5 | Първи кадър: медиана 3707 мс, последен 1754 мс | Пълно показване: медиана 5101 мс, последно 3240 мс | Топъл старт · 3 | Първи кадър: медиана 704 мс, последен 697 мс | Насечени кадри по екрани | article: 14,9 % от 94 кадъра · най-бавен 646 м
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 5" text="Първи кадър: медиана 3707 мс, последен 1754 мс" text="Пълно показване: медиана 5101 мс, последно 3240 мс" text="Топъл старт · 3" text="Първи кадър: медиана 704 мс, последен 697 мс" text="Насечени кад
APP W/E lines (pids ['5201', '7556', '13407', '18870', '23775']): 753 in 90 groups
      51  E/OpenGLRenderer: Unable to match the desired swap behavior.
      47  W/Parcel: Expecting binder but got null!
      17  W/BasicSecureTextField: 	at o#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
      17  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
      14  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       9  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       9  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       9  W/BasicSecureTextField: 	at ci#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       9  W/BasicSecureTextField: 	at o#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       9  W/BasicSecureTextField: 	at cs#.c(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       9  W/BasicSecureTextField: 	at p#.G#(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       9  W/BasicSecureTextField: 	at p#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       9  W/BasicSecureTextField: 	at xx.c(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       9  W/BasicSecureTextField: 	at rr#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       9  W/BasicSecureTextField: 	at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:#)
       9  W/BasicSecureTextField: 	at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:#)
       8  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       8  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       8  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       8  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       8  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       8  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       8  W/BasicSecureTextField: 	at h#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at vy#.b(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at vy#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at jr#.invoke(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at sn#.c(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at sn#.invoke(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       8  W/BasicSecureTextField: 	at g#.b(Unknown Source:#)
contact sheets: 26 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5201', '7556', '13407', '18870', '23775']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 90
   skipped-frames events=90 max=125 total=3900
== Davey: 245
   davey events=245 max=3140ms median=852ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## tour-c

```
part tour-c; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3861 ms
## T-w320dp. All screens (1080x2400 px, density 540, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP w320dp/01-home: targets=12 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=18 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP w320dp/01b-home-2: targets=14 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=1 [Търси книга, новина, събитие…] | Лента за търсене · Търси книга, новина, събитие · Сканирай баркод · 12 ноември 2026 · Св. Йоан М · Последни новини · БИБЛИОТЕКА
CAP w320dp/01c-home-3: targets=16 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Последни новини · 11 ноември 2026 · Сканирай · Читателска карта · Искам да прочета · Контакти
CAP w320dp/01d-home-4: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=24 ellipsized=0 | КАЛЕНДАР · Предстоящи събития · Всички · 16 · НОЕ · 80 г. от създаването на ЮНЕС
CAP w320dp/01e-search-12: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP w320dp/02-catalog: targets=19 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=36 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · 2446 резултата  ·  данни от 
CAP w320dp/02b-filters: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=16 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP w320dp/02c-filters-2: targets=16 MISSING=1 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Манипулатор за преместване с · друго · електронен документ · графично издание · аудиодокумент · продължаващо издание
CAP w320dp/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP w320dp/02e-shelf: targets=30 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=7 | front: app | texts=32 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · 2446 резултата  ·  данни от 
CAP w320dp/02f-shelf-2: targets=38 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=16 | front: app | texts=39 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · Алтъна, Никова, Ивелина
CAP w320dp/02g-search-12: targets=19 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · 12 -те вятъра
CAP w320dp/03-book: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP w320dp/03b-book-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=28 ellipsized=0 | Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство · Сиела, Русе · Година
CAP w320dp/03c-book-3: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Издателство · Сиела, Русе · Година · 1972 · ISBN · 9789546551135
CAP w320dp/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Черният зимата  · Още една · Към книгата · Затвори
CAP w320dp/05-my: targets=10 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CAP w320dp/05b-my-2: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Читателска карта · Добави номера от своята карт · Моите книги · Заети книги и срокове за връ · Искам да прочета · Книги, които искаш да вземеш
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP w320dp/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP w320dp/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP w320dp/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP w320dp/08-news: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP w320dp/08b-news-2: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=22 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP w320dp/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP w320dp/09b-article-2: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=3 ellipsized=0 | Групата за народни танци при · Групата за народни танци при · Назад
CAP w320dp/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP w320dp/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP w320dp/10-calendar: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP w320dp/11-event: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Панайот Пипков · Ден на семе · 21 ноември 2026 · Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана
CAP w320dp/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 21 ноември 2026 · Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP w320dp/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP w320dp/12-more: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP w320dp/12b-more-2: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Групи, клубове, проекти · За читалището · История и мисия · Галерия · Снимки от сайта · Документи и архив
CAP w320dp/13-gallery: targets=3 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | 29 снимки · Фотодокументална изложба · Назад · Галерия
CAP w320dp/14-album: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP w320dp/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP w320dp/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP w320dp/16-contacts: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP w320dp/16b-contacts-2: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP w320dp/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP w320dp/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP w320dp/18-settings: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP w320dp/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Злато и жито · Зелено и сребро · Червено, черно и слонова кос · Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета
CAP w320dp/18c-settings-3: targets=7 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Език · Български · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“ · Разклати телефона в каталога
CAP w320dp/18d-settings-4: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Разклати телефона в каталога · Известия · Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване
CAP w320dp/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP w320dp/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP w320dp/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP w320dp/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP w320dp/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP w320dp/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP w320dp/18k-about: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP w320dp/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 2
CAP w320dp/18m-diagnostics-2: targets=2 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=12 ellipsized=0 | Бързодействие · Времената на стартиране и пл · Студен старт · 2 · Първи кадър: медиана 3289 мс · Пълно показване: медиана 397 · Топъл старт · 1
CAP w320dp/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP w320dp/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR w320dp: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=18 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=39
## T-tablet-1280x800dp. All screens (2560x1600 px, density 320, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP tablet-1280x800dp/01-home: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=1 [Търси книга, новина, събитие…] | Начало · Каталог · Моето · Новини · Още · НЧ „Васил Левски – 1922“
CAP tablet-1280x800dp/01b-home-2: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Последни новини
CAP tablet-1280x800dp/01c-home-3: targets=25 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=44 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Моите книги
CAP tablet-1280x800dp/01d-home-4: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · БИБЛИОТЕКА
CAP tablet-1280x800dp/02-catalog: targets=23 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Търси навсякъде
CAP tablet-1280x800dp/02b-filters: targets=32 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=38 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ
CAP tablet-1280x800dp/02c-filters-2: targets=32 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=6 | front: app | texts=38 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · книга · видеодокумент · картографско издание · друго
CAP tablet-1280x800dp/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP tablet-1280x800dp/02e-shelf: targets=57 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=17 | front: app | texts=60 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Търси навсякъде
CAP tablet-1280x800dp/02f-shelf-2: targets=57 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=17 | front: app | texts=60 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Търси навсякъде
CAP tablet-1280x800dp/02g-search-12: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=45 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · 12
CAP tablet-1280x800dp/03-book: targets=28 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=70 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · 12
CAP tablet-1280x800dp/03b-book-2: targets=28 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=70 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · 12
CAP tablet-1280x800dp/03c-book-3: targets=28 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=70 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · 12
CAP tablet-1280x800dp/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Последният мечт · Още една · Към книгата · Затвори
CAP tablet-1280x800dp/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Здравей!
CAP tablet-1280x800dp/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Здравей!
CAP tablet-1280x800dp/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP tablet-1280x800dp/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP tablet-1280x800dp/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP tablet-1280x800dp/08-news: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Новини
CAP tablet-1280x800dp/08b-news-2: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=6 | front: app | texts=14 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Новини
CAP tablet-1280x800dp/09-article: targets=24 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=6 | front: app | texts=32 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Новини
CAP tablet-1280x800dp/09b-article-2: targets=24 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=6 | front: app | texts=32 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Новини
CAP tablet-1280x800dp/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP tablet-1280x800dp/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP tablet-1280x800dp/10-calendar: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=43 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Новини
CAP tablet-1280x800dp/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP tablet-1280x800dp/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 80 г. от създаването на ЮНЕС · 16 ноември 2026 · Събитие от годишния календар · Библиотека · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра
CAP tablet-1280x800dp/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP tablet-1280x800dp/12-more: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
CAP tablet-1280x800dp/12b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
CAP tablet-1280x800dp/13-gallery: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP tablet-1280x800dp/14-album: targets=30 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP tablet-1280x800dp/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Снимката липсва на сайта. · Затвори · 12 / 29  ·  Пастири и стада · Сподели · Отвори в сайта · За снимката
CAP tablet-1280x800dp/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Снимката липсва на сайта. · Затвори · 12 / 29  ·  Пастири и стада · Пастири и стада · Сподели · Отвори в сайта
CAP tablet-1280x800dp/16-contacts: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP tablet-1280x800dp/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл · chitalishte_yavorec@abv.bg · Работно време · Понеделник: 08:00 – 16:30
CAP tablet-1280x800dp/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP tablet-1280x800dp/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP tablet-1280x800dp/18-settings: targets=11 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP tablet-1280x800dp/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP tablet-1280x800dp/18c-settings-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известия · Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два 
CAP tablet-1280x800dp/18d-settings-4: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Корици от интернет · Корици по ISBN от covers.ope · Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни · Новините, събитията, каталог
CAP tablet-1280x800dp/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP tablet-1280x800dp/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP tablet-1280x800dp/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP tablet-1280x800dp/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP tablet-1280x800dp/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP tablet-1280x800dp/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP tablet-1280x800dp/18k-about: targets=3 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP tablet-1280x800dp/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 3
CAP tablet-1280x800dp/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 3
CAP tablet-1280x800dp/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP tablet-1280x800dp/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP tablet-1280x800dp/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR tablet-1280x800dp: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=7 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=62
## T-tablet-portrait. All screens (1600x2560 px, density 320, font_scale 1.0, night no, app locale [bg-BG], rotation 1)
CAP tablet-portrait/01-home: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=46 ellipsized=1 [Търси книга, новина, събитие…] | Начало · Каталог · Моето · Новини · Още · НЧ „Васил Левски – 1922“
CAP tablet-portrait/01b-home-2: targets=29 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=53 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Последни новини
CAP tablet-portrait/01c-home-3: targets=24 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Дъхът на спомените — том 3
CAP tablet-portrait/01d-home-4: targets=24 MISSING=5 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · НАЙ-ЧЕТЕНИ И НОВИ
CAP tablet-portrait/02-catalog: targets=28 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=79 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Търси навсякъде
CAP tablet-portrait/02b-filters: targets=33 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=42 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ
CAP tablet-portrait/02c-filters-2: targets=33 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=42 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ
CAP tablet-portrait/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP tablet-portrait/02e-shelf: targets=57 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=17 | front: app | texts=60 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Търси навсякъде
CAP tablet-portrait/02f-shelf-2: targets=57 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=17 | front: app | texts=60 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Търси навсякъде
CAP tablet-portrait/02g-search-12: targets=29 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=73 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · 12
CAP tablet-portrait/03-book: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | 12 -те вятъра · Гарфилд, Джудит и др. · Налична · Наличност към 09.11.2026 · Искам да прочета · Библиографски данни
CAP tablet-portrait/03b-book-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | 12 -те вятъра · Гарфилд, Джудит и др. · Налична · Наличност към 09.11.2026 · Искам да прочета · Библиографски данни
CAP tablet-portrait/03c-book-3: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | 12 -те вятъра · Гарфилд, Джудит и др. · Налична · Наличност към 09.11.2026 · Искам да прочета · Библиографски данни
CAP tablet-portrait/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Черният изгрева · Още една · Към книгата · Затвори
CAP tablet-portrait/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Здравей!
CAP tablet-portrait/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Здравей!
CAP tablet-portrait/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP tablet-portrait/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP tablet-portrait/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP tablet-portrait/08-news: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=31 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Новини
CAP tablet-portrait/08b-news-2: targets=30 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=7 | front: app | texts=38 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Новини
CAP tablet-portrait/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP tablet-portrait/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP tablet-portrait/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP tablet-portrait/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP tablet-portrait/10-calendar: targets=24 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=66 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Новини
CAP tablet-portrait/11-event: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Световен ден за борба срещу  · 1 декември 2026 · Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · Информационна инициатива за 
CAP tablet-portrait/11b-event-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Световен ден за борба срещу  · 1 декември 2026 · Събитие от годишния календар · Читалище · НЧ „Васил Левски – 1922“, пл · Информационна инициатива за 
CAP tablet-portrait/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP tablet-portrait/12-more: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
CAP tablet-portrait/12b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
CAP tablet-portrait/13-gallery: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP tablet-portrait/14-album: targets=30 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP tablet-portrait/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Снимката липсва на сайта. · Затвори · 8 / 29  ·  Стопанският двор  · Сподели · Отвори в сайта · За снимката
CAP tablet-portrait/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Снимката липсва на сайта. · Затвори · 8 / 29  ·  Стопанският двор  · Стопанският двор на ТКЗС в Я · Сподели · Отвори в сайта
CAP tablet-portrait/16-contacts: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=28 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP tablet-portrait/16b-contacts-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=28 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP tablet-portrait/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP tablet-portrait/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP tablet-portrait/18-settings: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP tablet-portrait/18b-settings-2: targets=13 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“ · Разклати телефона в каталога · Известия · Избери известията
CAP tablet-portrait/18c-settings-3: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | Известия · Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два 
CAP tablet-portrait/18d-settings-4: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | Известия · Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два 
CAP tablet-portrait/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP tablet-portrait/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP tablet-portrait/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP tablet-portrait/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP tablet-portrait/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 10 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP tablet-portrait/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP tablet-portrait/18k-about: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.35 (1507364) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP tablet-portrait/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 4
CAP tablet-portrait/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Студен старт · 4
CAP tablet-portrait/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP tablet-portrait/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP tablet-portrait/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR tablet-portrait: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=9 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=47
## T. Accessibility over all screens of this part
A11Y AGG: 168 screens analysed, 0 dumps failed; totals MISSING=34 DOUBLE=0 SAME-LABEL=15 SMALL(<48dp)=148
== MISSING: 30 distinct
   View@[1028,518][1080,680]  [4 screens: 02-catalog, 02e-shelf, 02f-shelf-2, 02g-search-12]
   View@[1031,1463][1127,1499]  [2 screens: 09-article, 09b-article-2]
   View@[160,1451][2560,1547]  [1 screens: 02-catalog]
   View@[160,514][2560,1480]  [1 screens: 08-news]
   View@[160,338][2560,1480]  [1 screens: 08b-news-2]
   View@[0,1479][1440,1575]  [1 screens: 18-settings]
   View@[48,1470][1488,1566]  [1 screens: 18k-about]
   View@[192,0][448,90]  [1 screens: 01d-home-4]
   View@[476,0][732,90]  [1 screens: 01d-home-4]
   View@[760,0][1016,90]  [1 screens: 01d-home-4]
   View@[1044,0][1300,90]  [1 screens: 01d-home-4]
   View@[1328,0][1584,90]  [1 screens: 01d-home-4]
   View@[160,2421][1600,2517]  [1 screens: 02g-search-12]
   View@[160,2366][1600,2462]  [1 screens: 08b-news-2]
   View@[1472,2426][1568,2462]  [1 screens: 08b-news-2]
   View@[0,2428][1440,2524]  [1 screens: 18b-settings-2]
   View@[54,1911][540,2049]  [1 screens: 01-home]
   View@[540,1911][1026,2049]  [1 screens: 01-home]
   View@[54,2019][999,2049]  [1 screens: 01b-home-2]
   View@[1040,2019][1080,2049]  [1 screens: 01b-home-2]
   View@[1040,1141][1080,1537]  [1 screens: 01c-home-3]
   View@[1040,184][1080,580]  [1 screens: 01d-home-4]
   View@[0,1942][1080,2049]  [1 screens: 02-catalog]
   View@[68,2301][464,2400]  [1 screens: 02b-filters]
   View@[68,2267][613,2400]  [1 screens: 02c-filters-2]
== SMALL: 32 distinct
   ''  [6 screens: 01-home, 01b-home-2, 08b-news-2, 09-article, 09b-article-2, 18m-diagnostics-2]
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   'Всички'  [3 screens: 08b-news-2, 09-article, 09b-article-2]
   'Запазени'  [3 screens: 08b-news-2, 09-article, 09b-article-2]
   'Библиотека'  [3 screens: 08b-news-2, 09-article, 09b-article-2]
   'Събитие'  [3 screens: 08b-news-2, 09-article, 09b-article-2]
   'Състави'  [3 screens: 08b-news-2, 09-article, 09b-article-2]
   '12 -те вятъра, Гарфилд, Джудит'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Аз обичам Ню Йорк, Келк, Линдз'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Ако върнеш времето назад, Потъ'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Алтъна, Никова, Ивелина'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Анатомът, Андахази, Федерико'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Бевълстоук : Кн. 1 - 2, Куин, '  [2 screens: 02e-shelf, 02f-shelf-2]
   'Безсрамна добродетел, Робъртс,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият дома, Николай Симеонов,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият звездите, Диана Христов'  [2 screens: 02e-shelf, 02f-shelf-2]
   'книга'  [1 screens: 02c-filters-2]
   'видеодокумент'  [1 screens: 02c-filters-2]
   'картографско издание'  [1 screens: 02c-filters-2]
   'друго'  [1 screens: 02c-filters-2]
   'Проекти'  [1 screens: 08b-news-2]
   'Запази: С хоро и песен във Вод'  [1 screens: 08-news]
   'Пътят на пътя'  [1 screens: 01d-home-4]
   'Огънят на здрача'  [1 screens: 01d-home-4]
   'Всичко'  [1 screens: 02f-shelf-2]
== SAME-LABEL: 1 distinct
   'Манипулатор за преместване с п' x2  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
== DOUBLE: 0 distinct
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 4 | Първи кадър: медиана 1943 мс, последен 1021 мс | Пълно показване: медиана 2596 мс, последно 2073 мс | Топъл старт · 1 | Първи кадър: медиана 372 мс, последен 372 мс | Насечени кадри по екрани | home: 3,4 % от 236 кадъра · най-бавен 2139 мс 
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 4" text="Първи кадър: медиана 1943 мс, последен 1021 мс" text="Пълно показване: медиана 2596 мс, последно 2073 мс" text="Топъл старт · 1" text="Първи кадър: медиана 372 мс, последен 372 мс" text="Насечени кад
APP W/E lines (pids ['5668', '6714', '13557', '18638']): 430 in 89 groups
      34  E/OpenGLRenderer: Unable to match the desired swap behavior.
      29  W/Parcel: Expecting binder but got null!
      10  W/BasicSecureTextField: 	at o#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       9  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       8  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       6  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       6  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       6  W/BasicSecureTextField: 	at ci#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       6  W/BasicSecureTextField: 	at o#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       6  W/BasicSecureTextField: 	at cs#.c(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       6  W/BasicSecureTextField: 	at p#.G#(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       6  W/BasicSecureTextField: 	at p#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       6  W/BasicSecureTextField: 	at xx.c(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       6  W/BasicSecureTextField: 	at rr#.d(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       5  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
       5  W/BasicSecureTextField: 	at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:#)
       5  W/BasicSecureTextField: 	at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:#)
       4  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       4  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       4  W/Gralloc4: allocator #.x is not supported
       4  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       4  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       4  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       4  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       4  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       4  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       4  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       4  W/BasicSecureTextField: 	at h#.a(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
       4  W/BasicSecureTextField: 	at vy#.b(r#-map-id-#a#f#e#bb#fa#d#ab#b#c#cdb#b#aa#bf#ddf#c#:#)
contact sheets: 19 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5668', '6714', '13557', '18638']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 37
   skipped-frames events=37 max=115 total=1609
== Davey: 105
   davey events=105 max=3669ms median=811ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## upd35

```
part upd35; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## U0. v1.2.34 (3812d8e, versionCode 1504705, staged update.json → the built 1.2.35, 1507364)
staged manifest: 1507364 1.2.35 4317894
installed base-upd: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/update/43302082995/update.json size 4316454;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
## U1. Using 1.2.34: „По-късно“ on the update dialog, text size „Голям“, dark theme, a card, a book in „Искам да прочета“, the widget
UPDATE first dialog after 24574 ms: text="Нова версия 1.2.35" text="Новата версия е изтеглена и проверена (контролна сума и подпис)." text="Какво ново" text="• По-бърз старт и по-плавно първо отваряне на читателската карта.&#10;• Дребни подобрения и поправки." text="По-късно" text="Инсталирай" 
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
TEXT SIZE now: text="Голям" 
theme options: text="Тема" text="Както системата" text="Светла" text="Тъмна" text="Затвори" 
WIDGET b134 long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET b134 picker: our row @ 437 935; size label @ 540 1843; texts: Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets widget|4 × 2|
WIDGET b134 after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; 
WIDGET b134: widgets of ours bound: 0 -> 1 (ids id=2 ); options: 
WIDGET v1.2.34: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|; error text: 
STATE before settings: text="Тъмна" text="Голям" 
STATE before night mode of the app window: ; app locale [bg-BG]
STATE before card: shown (TEST-12345)
STATE before want-to-read: book listed; text="Наличност към 09.11.2026" text="Плъзни книга настрани, за да я премахнеш." text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text=
## U2. Settings › Обновления on 1.2.34: the ready card → „Инсталирай“ → the system installer
downloaded files: 4317894 2026-11-12 20:50 update-1507364.apk;
relaunch after 'По-късно': dialog not shown
SETTINGS › Обновления: text="Версия 1.2.35 е готова" text="Изтеглена и проверена — инсталирането отнема само миг." 
after the tap on Инсталирай: text="Thu, Nov 12" content-desc="Thu, Nov 12" content-desc="Chitalishte Yavorets" text="Читалище Яворец" text="Сканирай" text="Каталог" text="Предстоящи събития" text="80 г. от създаването на ЮНЕСКО" 
UPDATE via the settings card: installed  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
## U3. After the update: the widget before the app is opened, „Какво ново“, then what survived
WIDGET after the update (app not opened): views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|; error text: 
widget ids before/after: [id=2 ] / [id=2 ]
WHATS-NEW 30-whats-new: SHOWN: text="Какво ново във версия 1.2.35"
NOTES sheet-30-whats-new: 2/2 lines of the real 1.2.30 notes visible
relaunch: what's new again? no; update dialog not shown
STATE after settings: text="Тъмна" text="Голям" 
STATE after night mode of the app window: ; app locale [bg-BG]
STATE after card: shown (TEST-12345)
STATE after want-to-read: book listed; text="Наличност към 09.11.2026" text="Плъзни книга настрани, за да я премахнеш." text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text=
WIDGET after opening the app: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|; error text: 
WIDGET click catalog (widget_catalog @ 900 600) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
WIDGET click scan (widget_scan @ 694 600) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак сл
WIDGET click event (widget_event @ 540 786) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
WIDGET click app (t:Chitalishte Yavorets|Читалище Яворец @ 337 599) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
SETTINGS › Обновления after the update: text="Провери за нова версия" 
leftover update files: ''
contact sheets: 4 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 3 | Първи кадър: медиана 2335 мс, последен 2145 мс | Пълно показване: медиана 3264 мс, последно 3515 мс | Топъл старт · 6 | Първи кадър: медиана 648 мс, последен 649 мс | Пълно показване: медиана 3719 мс, последно 4391 мс | Насечени кадри по ек
DIAG SCREEN (scrolled): text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 3" text="Първи кадър: медиана 2335 мс, последен 2145 мс" text="Пълно показване: медиана 3264 мс, последно 3515 мс" text="Топъл старт · 6" text="Първи кадър: медиана 648 мс, последен 649 мс" text="Пълно показване: медиана 3719 мс, последно 4391 мс" text="Насечени кадри по екрани" text="home: 0,5 % от 185 кадъра · най-бавен 1107 мс" text="card: 0,0 % от 75 кадъра · най-бавен 242 мс" text="settings: 0,0 % от 39 кадъра · най-бавен 230 мс" text="more: 0,0 % от 32 кадъра · най-бавен 303 мс" text="Няма записани грешки." content-desc="Назад" text="Диа
APP W/E lines (pids ['5417', '8992', '9767', '10338', '10639']): 70 in 14 groups
      14  E/OpenGLRenderer: Unable to match the desired swap behavior.
      10  W/Parcel: Expecting binder but got null!
       7  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
       6  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       5  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       5  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       5  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       5  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       5  W/Gralloc4: allocator #.x is not supported
       3  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
       2  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object g#.s(java.lang.Object)(r
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#f#bc-#b#-#b#-#a#c-#ebcaf#, generation=#)
       1  W/PackageParser: Unknown element under <manifest>: queries at /data/user/#/org.chyavorec.app/no_backup/updates/update-#.apk.par
       1  W/LibraryVersion: Failed to get app version for libraryName: play-services-code-scanner
contact sheets: 5 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5417', '8992', '9767', '10338', '10639']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 37
   skipped-frames events=37 max=94 total=1902
== Davey: 74
   davey events=74 max=2514ms median=1096ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 83123049 Nov 12 20:46 app-mapping.txt; crashes: 0 B
```

## year35

```
part year35; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
installed: devDebug 1.2.35 aef8910 (org.chyavorec.app.dev, demo sign-in DEMO-0001);  versionCode=1 minSdk=26 targetSdk=37  versionName=1.2.35-dev 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 6350 ms
## Y0. Demo sign-in (DEMO-0001 / demo, „Запомни ме“)
Запомни ме before: CHECKSTATE 'Запомни ме': checked=true (node itself/ancestor, clickable=true)
Запомни ме now: CHECKSTATE 'Запомни ме': checked=true (node itself/ancestor, clickable=true)
MY after the demo sign-in: text="ДЕМО ДАННИ — само за разработка, не са реални" text="ДЧ" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" text="БИБЛИОТЕКА" text="Читателска карта" text="Покажи картата на гишето" 
## Y1. Моето (signed in) and its screens
CAP demo/01-my: targets=12 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | ДЕМО ДАННИ — само за разрабо · ДЧ · Демо Читател · Читателски № DEMO-0001 · Активно · БИБЛИОТЕКА
CAP demo/01b-my-2: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=23 ellipsized=0 | Книги, които искаш да вземеш · Статус и валидност · Прочетени книги · Историята на четенето и подо · Моят профил · Читателски данни
CAP demo/01c-my-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | МОЕТО ЧЕТЕНЕ · 2026 · Моята читателска година 2026 · 30 книги · виж историята · Книги по месеци · Цялата история
CAP demo/02-loans: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ДЕМО ДАННИ — само за разрабо · Последна синхронизация: Пред · Демо заглавие (просрочено) · Демо автор · Взета на 03.10.2026 · Срокът изтече на 09.11.2026
CAP demo/03-history: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | ДЕМО ДАННИ — само за разрабо · Всички години · 2026 · 2025 · 2026 · 30 книги
CAP demo/03b-history-2: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | ДЕМО ДАННИ — само за разрабо · взета 09.11.2026 · върната 1 · Чичовци · Вазов, Иван · взета 03.11.2026 · върната 1 · Железният светилник
CAP demo/04-messages: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 12 ноември 2026, 19:30 · Ново · Запазената книга пристигна · Демо: книгата, която запазих · Лично · 7 ноември 2026, 21:30
CAP demo/04b-message: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 12 ноември 2026, 19:30 · Ново · Запазената книга пристигна · Демо: книгата, която запазих · Лично · 7 ноември 2026, 21:30
## Y2. Year story: tap → first slide (raw screencaps), fresh process ×2 and reopen ×2
YR fresh-1: STORY window first seen: shot #3, 1180..1950 ms after the tap; FIRST SLIDE (content) seen: shot #3, 1180..1950 ms after the tap
YR fresh-1 shots: #1 [0..570] black m=0 s=0 | #2 [570..1180] light m=105 s=61 | #3 [1180..1950] SLIDE m=52 s=23 | #4 [1950..2820] SLIDE m=54 s=28 | #5 [2820..3900] SLIDE m=55 s=29 | #6 [3900..4900] SLIDE m=55 s=29 | #7 [4900..5820] SLIDE m=55 s=29 | #8 [5820..6380] SLIDE m=55 s=29 | #9 [6380..7600] SLIDE m=55 s=29 | #10 [7600..8000] SLIDE m=55 s=29 | #11 [8000..8660] SLIDE m=45 s=28 | #12 [8660..9120] SLIDE m=45 s=29 | #13 [9120..9880] SLIDE m=46 s=30 | #14 [9880..10910] SLIDE m=46 s=30 | #15 [10910..12120] SLIDE m=46 s=30 | #16 [12120..12970] SLIDE m=46 s=30 | #17 [12970..13570] SLIDE m=46 s=30 | #18 [13570..14420] SLIDE m=44 s=27 | #19 [14420..15130] SLIDE m=46 s=31 | #20 [15130..16230] SLIDE m=47 s=32 | #2
YR reopen-1: STORY window first seen: shot #2, 440..1280 ms after the tap; FIRST SLIDE (content) seen: shot #3, 1280..2030 ms after the tap
YR reopen-1 shots: #1 [0..440] black m=0 s=0 | #2 [440..1280] story-dark/loader m=0 s=7 | #3 [1280..2030] SLIDE m=54 s=27 | #4 [2030..2850] SLIDE m=55 s=29 | #5 [2850..4120] SLIDE m=55 s=29 | #6 [4120..4830] SLIDE m=55 s=29 | #7 [4830..5830] SLIDE m=55 s=29 | #8 [5830..6380] SLIDE m=55 s=29 | #9 [6380..7090] SLIDE m=55 s=29 | #10 [7090..7660] SLIDE m=55 s=29 | #11 [7660..8390] SLIDE m=55 s=29 | #12 [8390..9130] SLIDE m=45 s=28 | #13 [9130..10070] SLIDE m=45 s=30 | #14 [10070..11020] SLIDE m=46 s=30 | #15 [11020..12220] SLIDE m=46 s=30 | #16 [12220..12890] SLIDE m=46 s=30 | #17 [12890..13640] SLIDE m=46 s=30 | #18 [13640..14170] SLIDE m=46 s=30 | #19 [14170..14860] SLIDE m=44 s=26 | #20 [14860..15530] SLIDE m=4
YR fresh-2: STORY window first seen: shot #3, 1130..1940 ms after the tap; FIRST SLIDE (content) seen: shot #3, 1130..1940 ms after the tap
YR fresh-2 shots: #1 [0..530] black m=0 s=0 | #2 [530..1130] black m=0 s=0 | #3 [1130..1940] SLIDE m=74 s=66 | #4 [1940..2640] SLIDE m=54 s=27 | #5 [2640..3770] SLIDE m=55 s=29 | #6 [3770..4790] SLIDE m=55 s=29 | #7 [4790..5690] SLIDE m=55 s=29 | #8 [5690..6490] SLIDE m=55 s=29 | #9 [6490..7420] SLIDE m=55 s=29 | #10 [7420..8250] SLIDE m=55 s=29 | #11 [8250..9010] SLIDE m=42 s=24 | #12 [9010..9500] SLIDE m=45 s=29 | #13 [9500..10020] SLIDE m=46 s=30 | #14 [10020..11290] SLIDE m=46 s=30 | #15 [11290..12080] SLIDE m=46 s=30 | #16 [12080..12720] SLIDE m=46 s=30 | #17 [12720..13350] SLIDE m=46 s=30 | #18 [13350..14220] SLIDE m=46 s=30 | #19 [14220..15140] SLIDE m=45 s=28 | #20 [15140..15840] SLIDE m=47 s=31 | #21
YR reopen-2: STORY window first seen: shot #2, 660..1600 ms after the tap; FIRST SLIDE (content) seen: shot #2, 660..1600 ms after the tap
YR reopen-2 shots: #1 [0..660] black m=0 s=0 | #2 [660..1600] SLIDE m=74 s=65 | #3 [1600..2330] SLIDE m=54 s=27 | #4 [2330..3690] SLIDE m=55 s=29 | #5 [3690..4230] SLIDE m=55 s=29 | #6 [4230..5150] SLIDE m=55 s=29 | #7 [5150..6340] SLIDE m=55 s=29 | #8 [6340..7190] SLIDE m=55 s=29 | #9 [7190..8240] SLIDE m=55 s=29 | #10 [8240..8760] SLIDE m=38 s=23 | #11 [8760..9040] SLIDE m=38 s=23 | #12 [9040..9730] SLIDE m=45 s=29 | #13 [9730..10420] SLIDE m=46 s=30 | #14 [10420..11310] SLIDE m=46 s=30 | #15 [11310..11940] SLIDE m=46 s=30 | #16 [11940..12820] SLIDE m=46 s=30 | #17 [12820..13790] SLIDE m=46 s=30 | #18 [13790..14430] SLIDE m=46 s=30 | #19 [14430..14850] SLIDE m=43 s=24 | #20 [14850..15450] SLIDE m=45 s=28 | #
## Y3. Every slide (portrait, standard text): screenshot, scroll inside
YEAR port slide 1: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Моята читателска година · 2026 · Затвори · Сподели
  slide 1 after a slow drag up inside: diff 0.50
YEAR port slide 2: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Най-четен раздел: Език. Литература — 2 к · Моята читателска година · 2026 · Затвори · Сподели
  slide 2 after a slow drag up inside: diff 14.89
YEAR port slide 3: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Най-дълга поредица: 11 месеца подред с п · Моята читателска година · 2026 · Затвори · Сподели
  slide 3 after a slow drag up inside: diff 23.17
YEAR port slide 4: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Благодарим ти, че четеш с нас! Библиотек · Моята читателска година · 2026 · Затвори · Сподели
  slide 4 after a slow drag up inside: diff 0.14
YEAR port slide 5: YS state=- error=no scrollable=1 ellipsized=0 beyond-screen=0 [] | Искам да прочета · Книги, които искаш да вземеш · Моето членство · Статус и валидност · Прочетени книги · Историята на четенето и подобни книги · Моят профил · Читателски данни
  slide 5 after a slow drag up inside: diff 0.00
  story closed at slide 5
## Y4. Swipe-down dismiss: each swipe from the first slide of a freshly opened story, twice
YEAR2 new swipe d200t150 (525 px ≈ 200 dp in 150 ms): open closed
YEAR2 new swipe d200t300 (525 px ≈ 200 dp in 300 ms): open open
YEAR2 new swipe d200t600 (525 px ≈ 200 dp in 600 ms): closed closed
YEAR2 new swipe d120fast (315 px ≈ 120 dp in 70 ms): open open
YEAR2 new swipe d120slow (315 px ≈ 120 dp in 900 ms): open open
YEAR2 new swipe d40fast (105 px ≈ 40 dp in 45 ms): open open
YEAR2 new swipe d20fast (52 px ≈ 19 dp in 45 ms): open open
YEAR2 new swipe v133fast200 (1320 px ≈ 502 dp in 200 ms): closed closed
## Y5. Back key and the close button
YEAR back key: closed
YEAR close button: closed
## Y6. Dark theme: the first slides
YEAR dark slide 1: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | 2026 — твоята година с книгите · Моята читателска година · 2026 · Затвори · Сподели
  slide 1 after a slow drag up inside: diff 15.12
YEAR dark slide 2: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Любим автор: Иван Вазов — 6 книги през г · Моята читателска година · 2026 · Затвори · Сподели
  slide 2 after a slow drag up inside: diff 7.92
YEAR dark slide 3: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Рекорден месец: Септември — 5 книги. · Моята читателска година · 2026 · Затвори · Сподели
  slide 3 after a slow drag up inside: diff 16.88
YEAR dark slide 4: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Твоят читателски тип: Книжен дракон. Пог · Моята читателска година · 2026 · Затвори · Сподели
  slide 4 after a slow drag up inside: diff 28.06
YEAR dark slide 5: YS state=- error=no scrollable=1 ellipsized=0 beyond-screen=0 [] | Искам да прочета · Книги, които искаш да вземеш · Моето членство · Статус и валидност · Прочетени книги · Историята на четенето и подобни книги · Моят профил · Читателски данни
  slide 5 after a slow drag up inside: diff 0.00
YEAR dark slide 6: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | 2026 — твоята година с книгите · Моята читателска година · 2026 · Затвори · Сподели
  slide 6 after a slow drag up inside: diff 15.13
YEAR dark slide 7: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Любим автор: Иван Вазов — 6 книги през г · Моята читателска година · 2026 · Затвори · Сподели
  slide 7 after a slow drag up inside: diff 7.92
YEAR dark slide 8: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Рекорден месец: Септември — 5 книги. · Моята читателска година · 2026 · Затвори · Сподели
  slide 8 after a slow drag up inside: diff 16.89
YEAR dark slide 9: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Твоят читателски тип: Книжен дракон. Пог · Моята читателска година · 2026 · Затвори · Сподели
  slide 9 after a slow drag up inside: diff 5.40
## Y7. Share a slide as an image
YEAR share: fg android/com.android.internal.app.ChooserActivity; cache/share: 688161 2026-11-12 19:50 chyavorec-year-2026-author-1794513002246.png;
## Y8. Signed-in card: fullscreen (demo)
demo fullscreen card: yes
A11Y AGG: 8 screens analysed, 0 dumps failed; totals MISSING=1 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=2
== MISSING: 1 distinct
   View@[42,1995][524,2121]  [1 screens: 01-my]
== SMALL: 2 distinct
   'Книги, които искаш да вземеш'  [1 screens: 01b-my-2]
   'Статус и валидност'  [1 screens: 01b-my-2]
== SAME-LABEL: 1 distinct
   'Удължи срока' x2  [1 screens: 02-loans]
== DOUBLE: 0 distinct
contact sheets: 7 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 3 | Първи кадър: медиана 3673 мс, последен 3358 мс | Пълно показване: медиана 4813 мс, последно 4669 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 3" text="Първи кадър: медиана 3673 мс, последен 3358 мс" text="Пълно показване: медиана 4813 мс, последно 4669 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['5806', '8664', '9326']): 259 in 107 groups
       6  W/BasicSecureTextField: 	at androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.layout.LayoutNodeSubcompositionsState.subcompose(SubcomposeLayout.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:#)
       5  W/ziparchive: Unable to open '/data/app/~~Elb#og#L_#Ryts#uNf#WaQ==/org.chyavorec.app.dev-EGaPrexFSRFZ#oRQDaym#g==/base.dm': 
       4  W/Parcel: Expecting binder but got null!
       4  W/BasicSecureTextField: 	at androidx.compose.material#.SecureTextFieldKt$OutlinedSecureTextField$#.invoke(SecureTextField.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.InnerNodeCoordinator$LookaheadDelegateImpl.measure-BRTryo#(InnerNodeCoordinator.k
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.performMeasureBlock$lambda$#(LookaheadPassDelegate.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate$$ExternalSyntheticLambda#.invoke(D#$$SyntheticClass:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.performMeasure-BRTryo#$ui(LookaheadPassDelegate.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performLookaheadMeasure-BRTryo#$ui(LayoutNodeLayoutDeleg
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.remeasure-BRTryo#(LookaheadPassDelegate.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.measure-BRTryo#(LookaheadPassDelegate.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LayoutModifierNodeCoordinator$LookaheadDelegateForLayoutModifierNode.measure-BRTr
       4  W/BasicSecureTextField: 	at androidx.compose.ui.graphics.BlockGraphicsLayerModifier.measure-#p#s#s(GraphicsLayerModifier.kt:#)
       4  W/BasicSecureTextField: 	at android.view.View.draw(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       4  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
       4  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       3  W/yavorec.app.dev: Unexpected CPU variant for x#: x#_#.
       3  W/yavorec.app.dev: Known variants: atom, sandybridge, silvermont, kabylake, default
       3  W/yavorec.app.dev: Accessing hidden method Landroid/os/SystemProperties;->addChangeCallback(Ljava/lang/Runnable;)V (unsupported, 
       3  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       3  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       3  W/Gralloc4: allocator #.x is not supported
       3  E/OpenGLRenderer: Unable to match the desired swap behavior.
       3  W/yavorec.app.dev: Accessing hidden method Landroid/view/ViewTreeObserver;->dispatchOnScrollChanged()V (unsupported, reflection, 
       2  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
contact sheets: 8 in sheets/
## COMPLETE
## logcat findings
app pids seen: []
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 0
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## yup35

```
part yup35; APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v135full/no-update.json size 4316418 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
installed: devDebug 1.2.35 aef8910 (org.chyavorec.app.dev, demo sign-in DEMO-0001);  versionCode=1 minSdk=26 targetSdk=37  versionName=1.2.35-dev 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 5930 ms
Запомни ме before: CHECKSTATE 'Запомни ме': checked=true (node itself/ancestor, clickable=true)
Запомни ме now: CHECKSTATE 'Запомни ме': checked=true (node itself/ancestor, clickable=true)
MY after the demo sign-in: text="ДЕМО ДАННИ — само за разработка, не са реални" text="ДЧ" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" text="БИБЛИОТЕКА" text="Читателска карта" text="Покажи картата на гишето" 
## YU1. Swipe-down dismiss with exact distances (motionevent): is the finger-up position counted? (the rule: ≥140 dp at any speed)
YEAR3 A-moves-to-183-up-183 (UP at 183 dp; MOVE at: 23 46 69 92 115 138 183 dp): closed closed closed
YEAR3 B-last-move-114-up-183 (UP at 183 dp; MOVE at: 23 46 69 92 114 dp): open open open
YEAR3 C-moves-to-160-up-160 (UP at 160 dp; MOVE at: 40 80 120 160 dp): closed closed closed
YEAR3 D-one-move-20-up-183 (UP at 183 dp; MOVE at: 20 dp): open open open
YEAR3 E-moves-to-100-up-100 (UP at 100 dp; MOVE at: 25 50 75 100 dp): open open open
YEAR3 F-moves-to-139-up-139 (UP at 139 dp; MOVE at: 35 70 105 139 dp): open open open
contact sheets: 2 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 1 | Първи кадър: медиана 5273 мс, последен 5273 мс | Пълно показване: медиана 5273 мс, последно 5273 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 1" text="Първи кадър: медиана 5273 мс, последен 5273 мс" text="Пълно показване: медиана 5273 мс, последно 5273 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['5621']): 236 in 103 groups
       8  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       6  W/BasicSecureTextField: 	at androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.layout.LayoutNodeSubcompositionsState.subcompose(SubcomposeLayout.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:#)
       6  W/BasicSecureTextField: 	at android.view.View.draw(View.java:#)
       4  W/BasicSecureTextField: 	at androidx.compose.material#.SecureTextFieldKt$OutlinedSecureTextField$#.invoke(SecureTextField.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.InnerNodeCoordinator$LookaheadDelegateImpl.measure-BRTryo#(InnerNodeCoordinator.k
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.performMeasureBlock$lambda$#(LookaheadPassDelegate.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate$$ExternalSyntheticLambda#.invoke(D#$$SyntheticClass:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.performMeasure-BRTryo#$ui(LookaheadPassDelegate.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performLookaheadMeasure-BRTryo#$ui(LayoutNodeLayoutDeleg
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.remeasure-BRTryo#(LookaheadPassDelegate.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.measure-BRTryo#(LookaheadPassDelegate.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LayoutModifierNodeCoordinator$LookaheadDelegateForLayoutModifierNode.measure-BRTr
       4  W/BasicSecureTextField: 	at androidx.compose.ui.graphics.BlockGraphicsLayerModifier.measure-#p#s#s(GraphicsLayerModifier.kt:#)
       4  W/BasicSecureTextField: 	at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:#)
       4  W/BasicSecureTextField: 	at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:#)
       4  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       2  W/ziparchive: Unable to open '/data/app/~~M#K#z#sabkLz#FPH#VZqaw==/org.chyavorec.app.dev-ukRCGa#-HFZ#Q#nvqKUkDg==/base.dm': 
       2  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       2  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       2  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.PlatformPasswordVisibilitySettingImpl.getSystemShowPasswordSetting(BasicS
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextField_androidKt.rememberPlatformPasswordVisibilitySettings
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextFieldKt.BasicSecureTextField-ltb#GB#(BasicSecureTextField.
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextFieldKt.BasicSecureTextField-egD#TGM(BasicSecureTextField.
contact sheets: 3 in sheets/
## COMPLETE
## logcat findings
app pids seen: []
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 0
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 83123049 Nov 12 20:46 app-mapping.txt; crashes: 0 B
```

