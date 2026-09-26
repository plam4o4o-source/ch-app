# kotlinx.serialization — пазим генерираните сериализатори.
-keepattributes *Annotation*, InnerClasses, Signature, EnclosingMethod
-dontnote kotlinx.serialization.**
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** { kotlinx.serialization.KSerializer serializer(...); }
-keep,includedescriptorclasses class org.chyavorec.**$$serializer { *; }
-keepclassmembers class org.chyavorec.** { *** Companion; }
-keepclasseswithmembers class org.chyavorec.** { kotlinx.serialization.KSerializer serializer(...); }

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
