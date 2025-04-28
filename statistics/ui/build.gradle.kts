plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    id("kotlin-parcelize")
}

android {
    namespace = "com.d9tilov.android.statistics.ui"

    defaultConfig {
        vectorDrawables.useSupportLibrary = true
    }
}

dependencies {
    implementation(project(":category:domain:contract"))
    implementation(project(":category:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:model"))
    implementation(project(":statistics:domain:model"))
    implementation(project(":transaction:domain:contract"))
    implementation(project(":transaction:domain:model"))
    implementation(project(":transaction:ui"))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.material)
    implementation(libs.navigation.compose)
}
