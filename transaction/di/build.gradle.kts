plugins {
    id("moneymanager.android.library")
    id("moneymanager.di")
}

android {
    namespace = "com.d9tilov.android.transaction.di"
}

dependencies {
    implementation(project(":transaction:data:contract"))
    implementation(project(":transaction:data:impl"))
    implementation(project(":transaction:domain:contract"))
    implementation(project(":transaction:domain:impl"))
}
