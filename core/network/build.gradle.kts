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
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.d9tilov.android.network"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    compileSdk = compileSdkVersion


    defaultConfig {
        minSdk = minSdkVersion
        buildConfigField("String", "API_KEY", keystoreProperties["currency_api_key"] as String)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(project(":core:common"))

    implementation(libs.bundles.retrofit)
    implementation(libs.okHttp)
    implementation(libs.okHttpInterceptor)

    implementation(libs.coroutinesCore)

    implementation(libs.timber)

    implementation(libs.hilt)
    kapt(libs.hiltCompiler)
    kapt(libs.hiltAndroidCompiler)
}
