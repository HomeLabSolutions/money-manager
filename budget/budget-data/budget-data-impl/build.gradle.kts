plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.budget_data_impl"
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))

    implementation(project(":budget:budget-data:budget-data-contract"))
    implementation(project(":budget:budget-domain:budget-domain-model"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))

    implementation(libs.coroutinesCore)
}
