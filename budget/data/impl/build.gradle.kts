plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.budget.data.impl"
}

dependencies {
    implementation(project(":budget:data:contract"))
    implementation(project(":budget:domain:contract"))
    implementation(project(":budget:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(libs.kotlinx.coroutines.core)
}
