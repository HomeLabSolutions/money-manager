plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.analytics.data"
}

dependencies {

    implementation(project(":analytics:domain"))

    implementation(libs.firebase.analytics.ktx)
}
