plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.library.compose")
}

android {
    namespace = "com.d9tilov.android.analytics_data_impl"
}

dependencies {
    implementation(libs.composeRuntime)
    implementation(libs.firebaseAnalytics)
    implementation(platform(libs.firebaseBom))
}
