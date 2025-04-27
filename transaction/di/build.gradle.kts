

plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.transaction.di"
}

dependencies {
    implementation(project(":budget:domain:contract"))
    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":transaction:data:contract"))
    implementation(project(":transaction:data:impl"))
    implementation(project(":transaction:domain:contract"))
    implementation(project(":transaction:domain:impl"))
    implementation(project(":transaction:regular:domain:contract"))
    implementation(project(":user-info:domain:contract"))
    implementation(libs.hilt.android)
}
