# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.d9tilov.moneymanager.billing.domain.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.billing.domain.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.billing.domain.entity.** { *; }

-keep class com.d9tilov.moneymanager.backup.data.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.backup.data.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.backup.data.entity.** { *; }

-keep class com.d9tilov.moneymanager.user.data.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.user.data.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.user.data.entity.** { *; }

-keep class com.d9tilov.moneymanager.currency.data.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.currency.data.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.currency.data.entity.** { *; }

-keep class com.d9tilov.moneymanager.currency.data.local.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.currency.data.local.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.currency.data.local.entity.** { *; }

-keep class com.d9tilov.moneymanager.currency.data.remote.entity** { *; }
-keepclassmembers class com.d9tilov.moneymanager.currency.data.remote.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.currency.data.remote.entity.** { *; }

-keep class com.d9tilov.moneymanager.currency.domain.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.currency.domain.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.currency.domain.entity.** { *; }

-keep class com.d9tilov.moneymanager.category.data.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.category.data.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.category.data.entity.** { *; }

-keep class com.d9tilov.moneymanager.category.domain.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.category.domain.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.category.domain.entity.** { *; }

-keep class com.d9tilov.moneymanager.transaction.data.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.transaction.data.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.transaction.data.entity.** { *; }

-keep class com.d9tilov.moneymanager.transaction.domain.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.transaction.domain.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.transaction.domain.entity.** { *; }

-keep class com.d9tilov.moneymanager.budget.data.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.budget.data.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.budget.data.entity.** { *; }

-keep class com.d9tilov.moneymanager.budget.domain.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.budget.domain.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.budget.domain.entity.** { *; }

-keep class com.d9tilov.moneymanager.regular.data.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.regular.data.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.regular.data.entity.** { *; }

-keep class com.d9tilov.moneymanager.regular.domain.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.regular.domain.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.regular.domain.entity.** { *; }

-keep class com.d9tilov.moneymanager.goal.data.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.goal.data.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.goal.data.entity.** { *; }

-keep class com.d9tilov.moneymanager.goal.domain.entity.** { *; }
-keepclassmembers class com.d9tilov.moneymanager.goal.domain.entity.** { *; }
-keepnames @kotlin.Metadata class com.d9tilov.moneymanager.goal.domain.entity.** { *; }

-keep class * extends androidx.fragment.app.Fragment{}
-keepnames class androidx.navigation.fragment.NavHostFragment

-keepattributes *Annotation*
-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable
-keep class persistence.** {
  *;
}

-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.apache.**
-dontwarn org.w3c.dom.**

-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.* { *; }

-keep public class io.ktor.client.** {
    public <methods>;
    private <methods>;
}
-dontwarn kotlin.reflect.jvm.internal.**
-keep class kotlin.reflect.jvm.internal.** { *; }

-keep interface javax.annotation.Nullable

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.*
-keep class com.google.api.client.** {*;}

-dontwarn android.arch.util.paging.CountedDataSource
-dontwarn android.arch.persistence.room.paging.LimitOffsetDataSource

# Enum field names are used by the integrated EnumJsonAdapter.
# values() is synthesized by the Kotlin compiler and is used by EnumJsonAdapter indirectly
# Annotate enums with @JsonClass(generateAdapter = false) to use them with Moshi.
-keepclassmembers @com.squareup.moshi.JsonClass class * extends java.lang.Enum {
    <fields>;
    **[] values();
}

# Keep helper method to avoid R8 optimisation that would keep all Kotlin Metadata when unwanted
-keepclassmembers class com.squareup.moshi.internal.Util {
    private static java.lang.String getKotlinMetadataClassName();
}

-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature,RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,AnnotationDefault,InnerClasses, EnclosingMethod

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions.*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-dontwarn com.squareup.okhttp.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
#### OkHttp, Retrofit and Moshi
-dontwarn okhttp3.**
-dontwarn retrofit2.Platform.Java8
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}

-keep @com.squareup.moshi.JsonQualifier interface *
-keepclassmembers class * {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
