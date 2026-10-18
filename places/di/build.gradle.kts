plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.places.di"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":places:data:impl"))
    implementation(project(":places:domain:contract"))
    implementation(project(":places:domain:impl"))
    implementation(libs.retrofit)
}
