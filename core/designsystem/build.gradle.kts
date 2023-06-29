plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.d9tilov.android.designsystem"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion

        vectorDrawables.useSupportLibrary = true
    }

    dataBinding {
        isEnabled = true
    }

    buildFeatures {
        viewBinding = true
        compose = true
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

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))

    implementation(libs.material)
    implementation(libs.composeUi)
    implementation(libs.composeToolingPreview)
    implementation(libs.composeFoundation)
    implementation(libs.composeMaterial)
    implementation(libs.composeMaterialIconsCore)
    implementation(libs.composeMaterialIconsExtended)
    implementation(libs.composeUiTestManifest)
    implementation(libs.composeMaterial3)
    implementation(libs.core)
    debugImplementation(libs.composeToolingUi)

    implementation(libs.splashScreen)
}
