import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.d9tilov.android.network"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
        buildConfigField("String", "API_KEY", keystoreProperties["currency_api_key"] as String)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {

    implementation(project(":core:common"))

    implementation(libs.bundles.retrofit)
    implementation(libs.okHttp)
    implementation(libs.okHttpInterceptor)

    implementation(libs.timber)

    implementation(libs.hilt)
    kapt(libs.hiltAndroidCompiler)
}
