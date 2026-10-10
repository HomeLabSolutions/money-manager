plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.insights.di"
}

dependencies {
    implementation(project(":insights:data:contract"))
    implementation(project(":insights:data:impl"))
    implementation(project(":insights:domain:contract"))
    implementation(project(":insights:domain:impl"))
}
