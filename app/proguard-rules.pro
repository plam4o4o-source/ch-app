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

# ML Kit (скенер на баркодове): компонентите се създават чрез рефлексия от
# ComponentDiscovery по имена в манифеста. R8 в пълен режим маха празните
# конструктори, ако не са изрично запазени → „NoSuchMethodException <init>“ и
# срив при отваряне на скенера. Пазим конструкторите на всички регистратори.
-keep class * implements com.google.firebase.components.ComponentRegistrar { public <init>(); }
-keep class com.google.mlkit.**.*Registrar { public <init>(); }
-keep class com.google.mlkit.common.internal.MlKitComponentDiscoveryService { <init>(); }
-keep class com.google.mlkit.common.internal.MlKitInitProvider { <init>(); }

# CameraX: конфигурацията по подразбиране се зарежда по име от манифеста.
-keep class androidx.camera.camera2.Camera2Config$DefaultProvider { <init>(); }
