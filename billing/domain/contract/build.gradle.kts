plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.billing.domain.contract"
}

dependencies {
    implementation(libs.billing)
    implementation(project(":billing:domain:model"))

    implementation(libs.kotlinx.coroutines.core)
}
