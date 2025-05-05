plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.billing.di"
}

dependencies {

    implementation(project(":billing:data:contract"))
    implementation(project(":billing:data:impl"))
    implementation(project(":billing:domain:contract"))
    implementation(project(":billing:domain:impl"))
}
