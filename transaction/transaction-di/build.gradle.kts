

plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.transaction_di"
}

dependencies {

    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))

    implementation(project(":transaction:transaction-data:transaction-data-contract"))
    implementation(project(":transaction:transaction-data:transaction-data-impl"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))
    implementation(project(":transaction:transaction-domain:transaction-domain-impl"))

    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))

    implementation(project(":category:category-domain:category-domain-contract"))

    implementation(project(":user-info:user-domain:user-domain-contract"))

    implementation(project(":currency:currency-domain:currency-domain-contract"))

    implementation(project(":budget:budget-domain:budget-domain-contract"))
}
