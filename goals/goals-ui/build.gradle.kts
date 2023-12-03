plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
}

android {
    namespace = "com.d9tilov.android.goals_ui"
}

dependencies {
    implementation(project(":core:designsystem"))

    implementation(project(":budget:budget-domain:budget-domain-model"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))

    implementation(project(":goals:goals-di"))

    implementation(libs.kotlinDatetime)
    implementation(libs.constraintLayout)

    implementation(libs.composeUiTestManifest)

    implementation(libs.composeViewModel)

    implementation(libs.hiltNavigationCompose)
}
