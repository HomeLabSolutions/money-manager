plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.user.di"
}

dependencies {
    implementation(project(":user-info:data:contract"))
    implementation(project(":user-info:data:impl"))
    implementation(project(":user-info:domain:contract"))
    implementation(project(":user-info:domain:impl"))
    implementation(libs.koin.core)
}
