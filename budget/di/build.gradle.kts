plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.budget.di"
}

dependencies {
    implementation(project(":budget:data:contract"))
    implementation(project(":budget:data:impl"))
    implementation(project(":budget:domain:contract"))
    implementation(project(":budget:domain:impl"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":currency:domain:contract"))
}
