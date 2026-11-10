plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.budget.di"
}

dependencies {
    implementation(project(":budget:data:contract"))
    implementation(project(":budget:data:impl"))
    implementation(project(":budget:domain:contract"))
    implementation(project(":budget:domain:impl"))
    implementation(libs.koin.core)
}
