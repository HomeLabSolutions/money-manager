plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.home_ui"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
    }

    buildFeatures {
        viewBinding = true
    }

    dataBinding {
        isEnabled = true
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
    implementation(project(":core:common-android"))

    implementation(project(":billing:billing-domain:billing-domain-contract"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))
    implementation(project(":backup:backup-data:backup-data-impl"))

    implementation(project(":statistics:statistics-ui"))
    implementation(project(":profile:profile-ui"))
    implementation(project(":category:category-ui"))
    implementation(project(":incomeexpense:incomeexpense-ui"))

    implementation(libs.appCompat)
    implementation(libs.material)

    implementation(libs.hilt)
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.navigation)
    implementation(libs.timber)
}
