plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.billing_di"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
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

    implementation(project(":billing:billing-data:billing-data-contract"))
    implementation(project(":billing:billing-data:billing-data-impl"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))
    implementation(project(":billing:billing-domain:billing-domain-impl"))

    implementation(libs.hilt)
    
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.room)
}
