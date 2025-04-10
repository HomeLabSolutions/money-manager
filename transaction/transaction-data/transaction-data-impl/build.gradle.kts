plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.transaction_data_impl"
}

dependencies {
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":transaction:transaction-data:transaction-data-contract"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))
    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.paging.common)
}
