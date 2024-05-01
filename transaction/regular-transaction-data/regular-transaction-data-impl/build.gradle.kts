plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.regular_transaction_data_impl"
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":transaction:regular-transaction-data:regular-transaction-data-contract"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))
    implementation(project(":category:category-domain:category-domain-model"))

    implementation(libs.kotlinx.coroutines.core)
}
