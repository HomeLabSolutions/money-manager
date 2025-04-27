plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.category.di"
}

dependencies {
    implementation(project(":category:data:contract"))
    implementation(project(":category:data:impl"))
    implementation(project(":category:domain:contract"))
    implementation(project(":category:domain:impl"))
    implementation(libs.hilt.android)
}
