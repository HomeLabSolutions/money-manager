plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.insights.di"
}

dependencies {
    implementation(project(":insights:data:contract"))
    implementation(project(":insights:data:impl"))
    implementation(project(":insights:domain:contract"))
    implementation(project(":insights:domain:impl"))
    implementation(libs.koin.core)
}
