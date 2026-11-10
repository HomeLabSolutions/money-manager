plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.transaction.di"
}

dependencies {
    implementation(project(":transaction:data:contract"))
    implementation(project(":transaction:data:impl"))
    implementation(project(":transaction:domain:contract"))
    implementation(project(":transaction:domain:impl"))
    implementation(libs.koin.core)
}
