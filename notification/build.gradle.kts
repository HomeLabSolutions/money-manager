plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.notification"
}

dependencies {
    implementation(project(":core:common-android"))
    implementation(project(":notification:domain"))
    implementation(libs.androidx.core)
    implementation(libs.koin.core)
}
