plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.budget_data_impl"
}

dependencies {
    implementation(project(":budget:budget-data:budget-data-contract"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))
    implementation(project(":budget:budget-domain:budget-domain-model"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(libs.kotlinx.coroutines.core)
}
