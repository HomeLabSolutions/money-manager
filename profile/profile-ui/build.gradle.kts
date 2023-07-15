plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    id("moneymanager.android.library.viewbinding")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.d9tilov.android.profile_ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))

    implementation(project(":backup:backup-data:backup-data-impl"))

    implementation(project(":user-info:user-domain:user-domain-model"))
    implementation(project(":user-info:user-domain:user-domain-contract"))

    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-ui"))

    implementation(project(":budget:budget-domain:budget-domain-model"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))
    implementation(project(":budget:budget-ui"))

    implementation(project(":billing:billing-domain:billing-domain-contract"))

    implementation(project(":category:category-domain:category-domain-model"))

    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))
    implementation(project(":transaction:regular-transaction-ui"))

    implementation(project(":settings:settings-ui"))
    implementation(project(":goals:goals-ui"))
    implementation(project(":incomeexpense:incomeexpense-ui"))

    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.constraintLayout)
    implementation(libs.composeConstraintLayout)
    implementation(libs.coil)

    implementation(libs.composeUi)
    implementation(libs.composeToolingPreview)
    implementation(libs.composeFoundation)
    implementation(libs.composeMaterial)
    implementation(libs.composeUiTestManifest)
    implementation(libs.composeMaterial3)

    implementation(libs.composeViewModel)
    implementation(libs.composeRuntime)

    implementation(libs.hiltNavigationCompose)
    implementation(libs.firebaseAnalytics)
    implementation(platform(libs.firebaseBom))

    implementation(libs.firebaseUi)
    implementation(libs.googlePlayServicesAuth)
}
