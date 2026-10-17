# kotlinx.serialization: без собствени правила. Библиотеката носи официалните си
# (META-INF/com.android.tools/r8/*.pro: Companion/serializer() на @Serializable класовете),
# а кодът ни ползва само явни Foo.serializer() — R8 ги проследява сам. Старите широки
# правила (-keep на всички $$serializer, Companion на всеки клас в org.chyavorec.**,
# *Annotation*/Signature навсякъде) пречеха на R8 да съкращава и оптимизира.

# Jsoup — незадължителни зависимости.
-dontwarn org.jspecify.annotations.**
-dontwarn com.google.re2j.**

# OkHttp — платформени класове, които липсват на Android.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Премахване на отладъчните логове в release (без лични данни в logcat).
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# CameraX: конфигурацията по подразбиране се зарежда по име от манифеста.
-keep class androidx.camera.camera2.Camera2Config$DefaultProvider { <init>(); }
