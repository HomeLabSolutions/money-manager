plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.analytics.di"
}

dependencies {
    implementation(project(":analytics:data"))
    implementation(project(":analytics:domain"))
    implementation(project(":core:datastore"))
    implementation(libs.firebase.analytics)
    implementation(libs.koin.core)
}
