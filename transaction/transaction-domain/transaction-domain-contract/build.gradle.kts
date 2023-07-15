plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.transaction_domain_contract"

    
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(project(":category:category-domain:category-domain-model"))

    implementation(libs.coroutinesCore)
    implementation(libs.paging)
}
