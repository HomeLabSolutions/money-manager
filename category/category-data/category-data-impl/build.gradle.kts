plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.category_data_impl"

    
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))

    implementation(project(":category:category-data:category-data-contract"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":category:category-domain:category-domain-contract"))

    implementation(libs.coroutinesCore)
}
