plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.billing.domain.contract"
}

dependencies {
    api(libs.billing)
    implementation(project(":billing:domain:model"))

    implementation(libs.kotlinx.coroutines.core)
}
