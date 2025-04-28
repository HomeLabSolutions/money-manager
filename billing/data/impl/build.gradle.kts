plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.billing.data.impl"
}

dependencies {
    implementation(project(":billing:data:contract"))
    implementation(project(":billing:domain:contract"))
    implementation(project(":billing:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":currency:domain:model"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.timber)
}
