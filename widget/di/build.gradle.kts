plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.widget.di"
}

dependencies {
    implementation(project(":widget:domain:contract"))
    implementation(project(":widget:domain:impl"))
    implementation(project(":widget:ui"))
    implementation(libs.koin.core)
}
