plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.analytics_di"
}

dependencies {
    implementation(project(":core:datastore"))

    implementation(libs.firebaseAnalytics)
    implementation(platform(libs.firebaseBom))
}
