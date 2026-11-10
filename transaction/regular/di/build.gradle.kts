plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.transaction.regular.di"
}

dependencies {
    implementation(project(":category:domain:contract"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":transaction:regular:data:contract"))
    implementation(project(":transaction:regular:data:impl"))
    implementation(project(":transaction:regular:domain:contract"))
    implementation(project(":transaction:regular:domain:impl"))
    implementation(libs.koin.core)
}
