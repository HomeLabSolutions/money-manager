plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    id("moneymanager.android.library.viewbinding")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.d9tilov.android.incomeexpense_ui"

    defaultConfig {
        vectorDrawables.useSupportLibrary = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))

    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":category:category-ui"))

    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))

    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))
    implementation(project(":transaction:transaction-ui"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))

    implementation(project(":billing:billing-domain:billing-domain-contract"))
    implementation(project(":billing:billing-di"))

    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.paging)

    implementation(libs.navigation)
    implementation(libs.navigationCompose)

    implementation(libs.timber)

    implementation(libs.firebaseAnalytics)
    implementation(platform(libs.firebaseBom))
}
