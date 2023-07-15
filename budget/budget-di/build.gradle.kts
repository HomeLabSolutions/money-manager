plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    
}

android {
    namespace = "com.d9tilov.android.budget_di"


    
}

dependencies {

    implementation(project(":core:database"))
    implementation(project(":core:datastore"))

    implementation(project(":budget:budget-data:budget-data-contract"))
    implementation(project(":budget:budget-data:budget-data-impl"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))
    implementation(project(":budget:budget-domain:budget-domain-impl"))

    implementation(project(":currency:currency-domain:currency-domain-contract"))

    
    
}
