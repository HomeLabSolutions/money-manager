plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    id("moneymanager.android.library.viewbinding")
}

android {
    namespace = "com.d9tilov.android.splash_ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:network"))

    implementation(project(":user-info:user-domain:user-domain-model"))
    implementation(project(":user-info:user-data:user-data-impl"))
    implementation(project(":user-info:user-domain:user-domain-contract"))
    implementation(project(":user-info:user-di"))

    implementation(project(":backup:backup-domain:backup-domain-contract"))
    implementation(project(":backup:backup-di"))

    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":category:category-di"))

    implementation(project(":prepopulate:prepopulate-ui"))
    implementation(project(":home:home-ui"))

    implementation(libs.appCompat)
    implementation(libs.navigation)

    implementation(libs.composeViewModel)

    implementation(libs.firebaseUi)
    implementation(libs.googlePlayServicesAuth)

    implementation(libs.timber)

    implementation(libs.splashScreen)
}
