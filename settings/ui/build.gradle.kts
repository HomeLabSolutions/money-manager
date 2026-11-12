plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.library.compose")
    id("kotlin-parcelize")
    id("moneymanager.di")
}

android {
    namespace = "com.d9tilov.android.settings.ui"

    defaultConfig {
        vectorDrawables.useSupportLibrary = true
    }
}

dependencies {
    implementation(project(":analytics:domain"))
    implementation(project(":backup:domain:contract"))
    implementation(project(":backup:domain:model"))
    implementation(project(":billing:domain:contract"))
    implementation(project(":billing:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:network"))
    implementation(project(":insights:domain:contract"))
    implementation(project(":user-info:domain:contract"))
    implementation(project(":user-info:domain:model"))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.billing)
    implementation(libs.firebase.config)
    implementation(libs.firebase.ui.auth)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.metro.viewmodel)
    implementation(libs.metro.viewmodel.compose)
    implementation(libs.navigation.compose)
    implementation(libs.navigation.runtime)
    implementation(libs.timber)
}
