plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.viewbinding")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.d9tilov.android.home_ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))

    implementation(project(":billing:billing-domain:billing-domain-contract"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))
    implementation(project(":backup:backup-data:backup-data-impl"))

    implementation(project(":statistics:statistics-ui"))
    implementation(project(":profile:profile-ui"))
    implementation(project(":category:category-ui"))
    implementation(project(":transaction:transaction-ui"))
    implementation(project(":incomeexpense:incomeexpense-ui"))

    implementation(libs.appCompat)
    implementation(libs.material)

    implementation(libs.navigation)
    implementation(libs.timber)
}
