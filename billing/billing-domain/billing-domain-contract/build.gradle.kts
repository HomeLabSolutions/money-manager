plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.billing_domain_contract"

    
}

dependencies {

    implementation(project(":billing:billing-domain:billing-domain-model"))

    implementation(libs.coroutinesCore)
    api(libs.billing)
}
