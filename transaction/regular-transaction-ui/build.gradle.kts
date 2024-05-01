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
    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))

    implementation(libs.navigation)
    implementation(libs.glide)
    kapt(libs.glideCompiler)

    implementation(libs.material)

    implementation(libs.composeMaterial3)
    implementation(libs.navigationCompose)
    implementation(libs.hiltNavigationCompose)

    implementation(libs.composeViewModel)
    implementation(libs.composeRuntime)
    implementation(libs.bundles.compose)
    
}
