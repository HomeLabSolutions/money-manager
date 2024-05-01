plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    id("moneymanager.android.library.viewbinding")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.d9tilov.android.category_ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))

    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":category:category-data:category-data-impl"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":category:category-di"))

    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))
    implementation(project(":transaction:transaction-di"))

    implementation(project(":transaction:regular-transaction-di"))

    implementation(project(":billing:billing-domain:billing-domain-contract"))

    implementation(project(":analytics:analytics-di"))

    implementation(libs.glide)
    kapt(libs.glideCompiler)

    implementation(libs.navigation)
    implementation(libs.navigationUi)

    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.constraintLayout)

    implementation(libs.bundles.compose)
    implementation(libs.composeViewModel)
    implementation(libs.composeMaterial3)
    implementation(libs.composeRuntime)
    implementation(libs.hiltNavigationCompose)

    implementation(libs.firebaseAnalytics)
    implementation(platform(libs.firebaseBom))
}
