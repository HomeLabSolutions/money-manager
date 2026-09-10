plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.transaction.regular.impl"
}

dependencies {
    implementation(project(":category:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":transaction:domain:contract"))
    implementation(project(":transaction:regular:data:contract"))
    implementation(project(":transaction:regular:domain:contract"))
    implementation(project(":transaction:regular:domain:model"))
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime)
    implementation(libs.hilt.android)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.timber)
}
