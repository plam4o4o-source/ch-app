#!/usr/bin/env bash
# Сглобява помощното приложение (ВРЕМЕННО, само за емулатора) без Gradle: aapt2 + javac + d8 +
# apksigner от Android SDK на runner-а. $1 — изходният APK.
set -euo pipefail
OUTAPK=$(realpath -m "$1")
cd "$(dirname "$0")"
SDK=${ANDROID_HOME:-${ANDROID_SDK_ROOT:-/usr/local/lib/android/sdk}}
BT=$(ls -d "$SDK"/build-tools/*/ | sort -V | tail -1)
PLAT=$(ls -d "$SDK"/platforms/android-*/ | grep -E 'android-[0-9]+/$' | sort -V | tail -1)
echo "build-tools $BT platform $PLAT java $(javac -version 2>&1)"
W=$(mktemp -d)
"$BT/aapt2" compile --dir res -o "$W/res.zip"
"$BT/aapt2" link -o "$W/base.apk" -I "$PLAT/android.jar" --manifest AndroidManifest.xml "$W/res.zip" \
  --version-code 1 --version-name 1.0
mkdir -p "$W/classes" "$W/dex"
javac -source 11 -target 11 -nowarn -cp "$PLAT/android.jar" -d "$W/classes" $(find src -name '*.java')
"$BT/d8" --min-api 26 --lib "$PLAT/android.jar" --output "$W/dex" $(find "$W/classes" -name '*.class')
cp "$W/base.apk" "$W/unsigned.apk"
(cd "$W/dex" && zip -q "$W/unsigned.apk" classes.dex)
"$BT/zipalign" -f 4 "$W/unsigned.apk" "$W/aligned.apk"
keytool -genkeypair -noprompt -keystore "$W/k.jks" -storepass helper -keypass helper -alias h -keyalg RSA -keysize 2048 \
  -validity 3 -dname "CN=emulator helper" >/dev/null 2>&1
"$BT/apksigner" sign --ks "$W/k.jks" --ks-pass pass:helper --key-pass pass:helper --out "$OUTAPK" "$W/aligned.apk"
"$BT/aapt2" dump badging "$OUTAPK" | head -3
ls -l "$OUTAPK"
