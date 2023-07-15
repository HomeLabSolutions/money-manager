plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    
}

android {
    namespace = "com.d9tilov.android.billing_di"


    
}

dependencies {

    implementation(project(":billing:billing-data:billing-data-contract"))
    implementation(project(":billing:billing-data:billing-data-impl"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))
    implementation(project(":billing:billing-domain:billing-domain-impl"))

    
    
}
