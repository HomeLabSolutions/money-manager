plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.currency.di"
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":currency:data:contract"))
    implementation(project(":currency:data:impl"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:impl"))
    implementation(project(":currency:observer:contract"))

    implementation(project(":currency:observer:impl"))
    implementation(libs.retrofit)
}
