plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.common_android"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
    }

    dataBinding {
        isEnabled = true
    }

    buildFeatures {
        viewBinding = true
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

    api(libs.fragment)
    api(libs.appCompat)
    implementation(libs.paging)
    implementation(libs.material)
    implementation(libs.lifecycleRuntime)

    implementation(libs.coroutinesCore)

    implementation(libs.glide)
    kapt(libs.glideCompiler)

    implementation(libs.core)

    implementation(libs.navigation)
    implementation(libs.navigationUi)
}
