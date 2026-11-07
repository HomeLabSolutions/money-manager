plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.widget.di"
}

dependencies {
    implementation(project(":transaction:domain:contract"))
    implementation(project(":widget:domain:contract"))
    implementation(project(":widget:domain:impl"))
    implementation(project(":widget:ui"))
}
