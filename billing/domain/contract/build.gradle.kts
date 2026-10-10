plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.billing.domain.contract"
}

dependencies {
    implementation(project(":billing:domain:model"))
    implementation(libs.billing)
    implementation(libs.kotlinx.coroutines.core)
}
