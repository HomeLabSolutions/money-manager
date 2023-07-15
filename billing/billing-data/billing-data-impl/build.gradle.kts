plugins {
    id("moneymanager.android.library")
    
}

android {
    namespace = "com.d9tilov.android.billing_data_impl"

    
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":billing:billing-data:billing-data-contract"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))
    implementation(project(":billing:billing-domain:billing-domain-model"))

    implementation(project(":currency:currency-domain:currency-domain-model"))

    implementation(libs.coroutinesCore)
    implementation (libs.timber)

}
