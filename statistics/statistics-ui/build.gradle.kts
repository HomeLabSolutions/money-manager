plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    id("moneymanager.android.library.viewbinding")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.d9tilov.android.statistics_ui"

    defaultConfig {
        vectorDrawables.useSupportLibrary = true
    }
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))
    implementation(project(":statistics:statistics-domain:statistics-domain-model"))

    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))

    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))

    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))

    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.constraintLayout)

    implementation(libs.navigation)
    implementation(libs.navigationCompose)

    implementation(libs.glide)
    kapt(libs.glideCompiler)

    implementation(libs.androidChart)
}
