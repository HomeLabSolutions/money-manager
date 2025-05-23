plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.analytics.di"
}

dependencies {

    implementation(project(":analytics:data"))
    implementation(project(":analytics:domain"))
    implementation(project(":core:datastore"))
    implementation(libs.firebase.analytics.ktx)
}
