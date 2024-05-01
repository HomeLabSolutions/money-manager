plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.billing_domain_contract"
}

dependencies {

    implementation(project(":billing:billing-domain:billing-domain-model"))

    implementation(libs.kotlinx.coroutines.core)
    api(libs.billing)
}
