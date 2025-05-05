plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.billing.data.contract"
}

dependencies {
    implementation(libs.billing)
    implementation(libs.kotlinx.coroutines.core)
}
