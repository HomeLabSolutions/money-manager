plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    
}

android {
    namespace = "com.d9tilov.android.goals_di"

    
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":goals:goals-data:goals-data-contract"))
    implementation(project(":goals:goals-data:goals-data-impl"))
    implementation(project(":goals:goals-domain:goals-domain-contract"))
    implementation(project(":goals:goals-domain:goals-domain-impl"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))

    
    
}
