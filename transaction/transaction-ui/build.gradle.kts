plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    id("moneymanager.android.library.viewbinding")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.d9tilov.android.transaction_ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))

    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":category:category-ui"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))
    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))

    implementation(libs.glide)
    kapt(libs.glideCompiler)
    implementation(libs.navigation)
    implementation(libs.navigationCompose)
    implementation(libs.hiltNavigationCompose)

    implementation(libs.composeViewModel)
    implementation(libs.composeRuntime)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.composeMaterial3)

    implementation(libs.firebaseAnalytics)
    implementation(platform(libs.firebaseBom))

    implementation(libs.paging)
    implementation(libs.appCompat)
    implementation(libs.material)
}
