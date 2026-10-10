plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.insights.data.contract"
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":insights:domain:model"))
    implementation(project(":user-info:domain:model"))
    implementation(libs.kotlinx.coroutines.core)
}
