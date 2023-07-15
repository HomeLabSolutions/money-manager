plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
}

android {
    namespace = "com.d9tilov.android.prepopulate_ui"
}

dependencies {
    implementation(project(":core:common"))

    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-observer:currency-observer-contract"))
    implementation(project(":currency:currency-ui"))

    implementation(project(":budget:budget-domain:budget-domain-model"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))
    implementation(project(":budget:budget-ui"))
    
    implementation(project(":user-info:user-domain:user-domain-contract"))
    implementation(project(":user-info:user-di"))

    implementation(libs.composeUi)
    implementation(libs.composeToolingPreview)
    implementation(libs.composeFoundation)
    implementation(libs.composeMaterialIconsCore)
    implementation(libs.composeMaterialIconsExtended)
    implementation(libs.composeUiTestManifest)
    implementation(libs.composeMaterial3)

    implementation(libs.composeViewModel)
    implementation(libs.composeRuntime)

    implementation(libs.hiltNavigationCompose)
}
