plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.ui"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))

    implementation(libs.hiltNavigationCompose)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.composeMaterial3)
    implementation(libs.composeActivity)
    implementation(libs.composeViewModel)
    implementation(libs.composeRuntime)
    implementation(libs.composeConstraintLayout)
    debugImplementation(libs.composeToolingUi)

    implementation(libs.hilt)
    implementation(libs.hiltNavigationCompose)
    kapt(libs.hiltCompiler)
    kapt(libs.hiltAndroidCompiler)
}
