plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.transaction_data_impl"

    
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))
    implementation(project(":transaction:transaction-data:transaction-data-contract"))
    implementation(project(":category:category-domain:category-domain-model"))

    implementation(libs.roomPaging)
    implementation(libs.coroutinesCore)
}
