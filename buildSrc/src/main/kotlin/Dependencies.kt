object BuildPlugins {
    val android by lazy { "com.android.tools.build:gradle:${Versions.gradlePlugin}" }
    val kotlin by lazy { "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}" }
    val googlePlayServices by lazy { "com.google.gms:google-services:${Versions.googlePlayServices}" }
    val navigation by lazy { "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}" }
    val hilt by lazy { "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}" }
    val firebaseCrashlytics by lazy { "com.google.firebase:firebase-crashlytics-gradle:${Versions.firebaseCrashlytics}" }
    val detekt by lazy { "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}" }
}

/**
 * To define dependencies
 */
object Deps {
    val kotlinJdk by lazy { "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}" }
    val kotlinDatetime by lazy { "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinDatetime}" }

    val appCompat by lazy { "androidx.appcompat:appcompat:${Versions.appCompat}" }
    val material by lazy { "com.google.android.material:material:${Versions.material}" }
    val core by lazy { "androidx.core:core-ktx:${Versions.core}" }
    val coreRuntime by lazy { "androidx.arch.core:core-runtime:${Versions.coreRuntime}" }
    val constraintLayout by lazy { "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}" }

    val fragment by lazy { "androidx.fragment:fragment-ktx:${Versions.fragment}" }

    val glide by lazy { "com.github.bumptech.glide:glide:${Versions.glide}" }
    val glideCompiler by lazy { "com.github.bumptech.glide:compiler:${Versions.glide}" }

    val retrofit by lazy { "com.squareup.retrofit2:retrofit:${Versions.retrofit}" }
    val retrofitMoshi by lazy { "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}" }

    val roomRuntime by lazy { "androidx.room:room-runtime:${Versions.room}" }
    val room by lazy { "androidx.room:room-ktx:${Versions.room}" }
    val roomCompiler by lazy { "androidx.room:room-compiler:${Versions.room}" }
    val roomPaging by lazy { "androidx.room:room-paging:${Versions.room}" }
    val paging by lazy { "androidx.paging:paging-runtime-ktx:${Versions.paging}" }

    val sql by lazy { "androidx.sqlite:sqlite-ktx:${Versions.sql}" }
    val sqlCipher by lazy { "net.zetetic:android-database-sqlcipher:${Versions.sqlCipher}" }

    val navigation by lazy { "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}" }
    val navigationUi by lazy { "androidx.navigation:navigation-ui-ktx:${Versions.navigation}" }

    val firebase by lazy { "com.google.firebase:firebase-auth-ktx" }
    val firebaseAnalytics by lazy { "com.google.firebase:firebase-analytics-ktx" }
    val firebaseCrashlytics by lazy { "com.google.firebase:firebase-crashlytics-ktx" }
    val firebaseStorage by lazy { "com.google.firebase:firebase-storage-ktx" }
    val firebaseUi by lazy { "com.firebaseui:firebase-ui-auth:${Versions.firebaseUi}" }
    val firebaseBom by lazy { "com.google.firebase:firebase-bom:${Versions.firebaseBom}" }

    val googlePlayServicesAuth by lazy { "com.google.android.gms:play-services-auth:${Versions.googlePlayServicesAuth}" }
    val facebookLogin by lazy { "com.facebook.android:facebook-android-sdk:${Versions.facebookSdk}" }
    val twitter by lazy { "com.twitter.sdk.android:twitter:${Versions.twitter}" }

    val okHttp by lazy { "com.squareup.okhttp3:okhttp:${Versions.okhttp}" }
    val okHttpInterceptor by lazy { "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}" }

    val viewpager by lazy { "androidx.viewpager2:viewpager2:${Versions.viewpager}" }

    val coroutinesCore by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}" }

    val datastore by lazy { "androidx.datastore:datastore-preferences:${Versions.datastore}" }

    val lifecycle by lazy { "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}" }
    val lifecycleRuntime by lazy { "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}" }
    val lifecycleExtension by lazy { "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtension}" }
    val lifecycleViewModel by lazy { "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}" }
    val lifecycleCommon by lazy { "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}" }

    val worker by lazy { "androidx.work:work-runtime-ktx:${Versions.worker}" }
    val workerHilt by lazy { "androidx.hilt:hilt-work:${Versions.workerHilt}" }

    val klaxon by lazy { "com.beust:klaxon:${Versions.klaxon}" }
    val timber by lazy { "com.jakewharton.timber:timber:${Versions.timber}" }
    val ktlint by lazy { "com.pinterest:ktlint:${Versions.ktlint}" }

    val androidChart by lazy { "com.github.PhilJay:MPAndroidChart:${Versions.androidChart}" }
    val dotsIndicator by lazy { "com.tbuonomo:dotsindicator:${Versions.dotsIndicator}" }

    val billing by lazy { "com.android.billingclient:billing-ktx:${Versions.billing}" }

    val hilt by lazy { "com.google.dagger:hilt-android:${Versions.hilt}" }
    val hiltAndroidCompiler by lazy { "com.google.dagger:hilt-android-compiler:${Versions.hilt}" }

    val splashScreen by lazy { "androidx.core:core-splashscreen:${Versions.splashScreen}" }
}
