plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.datastore"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.datastore)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.core)
}
