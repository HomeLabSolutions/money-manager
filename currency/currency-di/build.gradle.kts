plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.currency_di"
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
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":currency:currency-data:currency-data-contract"))
    implementation(project(":currency:currency-data:currency-data-impl"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-domain:currency-domain-impl"))
    implementation(project(":currency:currency-observer:currency-observer-contract"))
    implementation(project(":currency:currency-observer:currency-observer-impl"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))

    implementation(libs.bundles.retrofit)

    implementation(libs.hilt)
    
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.bundles.room)
}
