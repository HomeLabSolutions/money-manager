plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
}

android {
    namespace = "com.d9tilov.android.insights.presentation"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.activity.compose)
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:datastore"))
    implementation(project(":insights:domain"))
    implementation(project(":core:designsystem"))
    implementation(project(":notification"))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.core)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime)
    implementation(libs.firebase.auth)
    implementation(libs.hilt.android)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.navigation.runtime)
    implementation(libs.timber)
}
