plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.transaction.regular.di"
}

dependencies {
    implementation(project(":category:category-domain:contract"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":transaction:regular:data:contract"))
    implementation(project(":transaction:regular:data:impl"))
    implementation(project(":transaction:regular:domain:contract"))
    implementation(project(":transaction:regular:domain:impl"))
    implementation(libs.hilt.android)
}
