plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.regular_transaction_di"
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":transaction:regular-transaction-data:regular-transaction-data-contract"))
    implementation(project(":transaction:regular-transaction-data:regular-transaction-data-impl"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-impl"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":category:category-domain:category-domain-contract"))
}
