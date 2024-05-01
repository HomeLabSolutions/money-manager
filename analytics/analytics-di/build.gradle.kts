plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.analytics_di"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(project(":core:datastore"))
    implementation(libs.hilt.android)
    implementation(libs.firebase.analytics)
}
