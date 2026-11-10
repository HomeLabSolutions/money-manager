plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.category.di"
}

dependencies {
    implementation(project(":category:data:contract"))
    implementation(project(":category:data:impl"))
    implementation(project(":category:domain:contract"))
    implementation(project(":category:domain:impl"))
    implementation(libs.koin.core)
}
