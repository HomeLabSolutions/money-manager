

plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.d9tilov.android.transaction.di"
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":transaction:data:contract"))
    implementation(project(":transaction:data:impl"))
    implementation(project(":transaction:domain:contract"))
    implementation(project(":transaction:domain:impl"))
    implementation(libs.hilt.android)

    ksp(libs.hilt.ext.compiler)
}
