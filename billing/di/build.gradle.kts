plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.billing.di"
}

dependencies {
    implementation(project(":billing:data:contract"))
    implementation(project(":billing:data:impl"))
    implementation(project(":billing:domain:contract"))
    implementation(project(":billing:domain:impl"))
    implementation(libs.koin.core)
}
