plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.library.compose")
    id("moneymanager.android.library.viewbinding")
}

android {
    namespace = "com.d9tilov.android.designsystem"

    defaultConfig {
        vectorDrawables.useSupportLibrary = true
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
