plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    id("moneymanager.android.library.viewbinding")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.d9tilov.android.settings_ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))

    implementation(project(":billing:billing-domain:billing-domain-model"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))

    implementation(project(":backup:backup-domain:backup-domain-contract"))
    implementation(project(":backup:backup-domain:backup-domain-model"))

    implementation(project(":user-info:user-domain:user-domain-model"))
    implementation(project(":user-info:user-domain:user-domain-contract"))

    implementation(project(":currency:currency-domain:currency-domain-model"))

    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.constraintLayout)
    implementation(libs.navigation)

    implementation(libs.composeUi)
    implementation(libs.composeToolingPreview)
    implementation(libs.composeFoundation)
    implementation(libs.composeUiTestManifest)
    implementation(libs.composeMaterial3)
    implementation(libs.composeViewModel)
    implementation(libs.composeRuntime)

    implementation(libs.timber)

    implementation(libs.hiltNavigationCompose)
    implementation(libs.glide)
    kapt(libs.glideCompiler)

    implementation(libs.dotsIndicator)

    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseConfig)
}
