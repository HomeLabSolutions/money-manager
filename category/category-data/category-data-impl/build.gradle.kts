plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.category_data_impl"
}

dependencies {
    implementation(project(":category:category-data:category-data-contract"))
    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
