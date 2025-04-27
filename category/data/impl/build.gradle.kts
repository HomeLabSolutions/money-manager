plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.category.data.impl"
}

dependencies {
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":category:category-domain:contract"))
    implementation(project(":category:data:contract"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
