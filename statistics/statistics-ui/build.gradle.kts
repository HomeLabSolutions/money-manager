plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.statistics_ui"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion

        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))
    implementation(project(":statistics:statistics-domain:statistics-domain-model"))

    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))

    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))

    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))

    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.constraintLayout)

    implementation(libs.navigation)
    implementation(libs.navigationCompose)

    implementation(libs.glide)
    kapt(libs.glideCompiler)

    implementation(libs.hilt)
    
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.androidChart)
}
