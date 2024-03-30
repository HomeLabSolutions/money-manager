plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
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

    implementation(libs.bundles.compose)
    implementation(libs.bundles.composeMaterial3)
    implementation(libs.composeViewModel)
    implementation(libs.composeRuntime)
    implementation(libs.hiltNavigationCompose)
}
