plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.backup.di"
}

dependencies {
    implementation(project(":backup:data:contract"))
    implementation(project(":backup:data:impl"))
    implementation(project(":backup:domain:contract"))
    implementation(project(":backup:domain:impl"))
    implementation(libs.koin.core)
}
