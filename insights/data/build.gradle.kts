plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.insights.data"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":insights:domain"))
    implementation(libs.kotlinx.coroutines.core)
}
