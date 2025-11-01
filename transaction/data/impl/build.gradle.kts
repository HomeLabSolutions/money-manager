plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.transaction.data.impl"
}

dependencies {
    implementation(project(":category:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":transaction:data:contract"))
    implementation(project(":transaction:domain:contract"))
    implementation(project(":transaction:domain:model"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.paging.common)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk.core)
}
