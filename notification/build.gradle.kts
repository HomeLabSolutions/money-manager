plugins {
    id("moneymanager.android.library")
    id("moneymanager.di")
}

android {
    namespace = "com.d9tilov.android.notification"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":notification:domain"))
    implementation(libs.androidx.core)
}
