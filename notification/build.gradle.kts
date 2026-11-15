plugins {
    id("moneymanager.android.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.d9tilov.android.notification"
}

dependencies {
    implementation(project(":core:common-android"))
    implementation(project(":notification:domain"))
    implementation(libs.androidx.core)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.kotlin.metadata.jvm)
}
