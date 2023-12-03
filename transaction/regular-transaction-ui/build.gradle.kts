plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    id("moneymanager.android.library.viewbinding")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.d9tilov.android.regular_transaction_ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))

    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))
    implementation(project(":category:category-domain:category-domain-model"))

    implementation(libs.navigation)
    implementation(libs.glide)
    kapt(libs.glideCompiler)

    implementation(libs.material)

    implementation(libs.navigationCompose)
    implementation(libs.hiltNavigationCompose)

    implementation(libs.composeViewModel)
    implementation(libs.composeRuntime)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.composeMaterial3)
}
