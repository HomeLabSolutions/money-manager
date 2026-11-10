plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.currency.di"
}

dependencies {
    implementation(project(":currency:data:contract"))
    implementation(project(":currency:data:impl"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:impl"))
    implementation(project(":currency:observer:contract"))
    implementation(project(":currency:observer:impl"))
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    implementation(libs.ktor.client.core)
}
