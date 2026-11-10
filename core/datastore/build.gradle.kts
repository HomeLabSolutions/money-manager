plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.datastore"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.datastore)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
}
