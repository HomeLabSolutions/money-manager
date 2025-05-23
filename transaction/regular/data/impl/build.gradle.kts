plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.transaction.regular.impl"
}

dependencies {
    implementation(project(":category:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":transaction:regular:data:contract"))
    implementation(project(":transaction:regular:domain:contract"))
    implementation(project(":transaction:regular:domain:model"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
