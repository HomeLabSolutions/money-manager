plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.backup.di"
}

dependencies {
    implementation(project(":backup:data:contract"))
    implementation(project(":backup:data:impl"))
    implementation(project(":backup:domain:contract"))
    implementation(project(":backup:domain:impl"))
}
