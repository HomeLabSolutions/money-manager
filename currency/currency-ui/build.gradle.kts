plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    id("kotlin-parcelize")
}

android {
    namespace = "com.d9tilov.android.currency_ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))

    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-observer:currency-observer-contract"))
    implementation(project(":currency:currency-di"))

    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.constraintLayout)

    implementation(libs.composeUi)
    
    implementation(libs.composeViewModel)
    implementation(libs.composeMaterial3)
    implementation(libs.composeMaterial3WindowSize)
    implementation(libs.composeRuntime)
    implementation(libs.composeToolingPreview)
    implementation(libs.hiltNavigationCompose)
}
