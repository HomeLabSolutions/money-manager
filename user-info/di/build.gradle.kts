plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.user.di"
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":user-info:data:contract"))
    implementation(project(":user-info:data:impl"))
    implementation(project(":user-info:domain:contract"))
    implementation(project(":user-info:domain:impl"))
}
