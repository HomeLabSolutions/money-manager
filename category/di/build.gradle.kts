plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.category.di"
}

dependencies {

    implementation(project(":category:category-data:category-data-contract"))
    implementation(project(":category:category-data:category-data-impl"))
    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":category:category-domain:category-domain-impl"))

    implementation(libs.hilt.android)
}
