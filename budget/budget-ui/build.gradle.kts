plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
}

android {
    namespace = "com.d9tilov.android.budget_ui"
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
}
