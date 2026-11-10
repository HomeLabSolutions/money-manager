plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.places.di"
}

dependencies {
    implementation(project(":places:data:contract"))
    implementation(project(":places:data:impl"))
    implementation(project(":places:domain:contract"))
    implementation(project(":places:domain:impl"))
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    implementation(libs.ktor.client.core)
}
