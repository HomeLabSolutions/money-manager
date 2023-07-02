plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.budget_ui"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))

    implementation(project(":budget:budget-domain:budget-domain-model"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))
    implementation(project(":budget:budget-di"))

    implementation(libs.constraintLayout)

    implementation(libs.composeUi)
    implementation(libs.composeToolingPreview)
    implementation(libs.composeMaterial)
    implementation(libs.composeUiTestManifest)

    implementation(libs.composeMaterial3)
    implementation(libs.composeViewModel)
    implementation(libs.composeRuntime)

    implementation(libs.hiltNavigationCompose)
    implementation(libs.hilt)
    kapt(libs.hiltAndroidCompiler)
}
