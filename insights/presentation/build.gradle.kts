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
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))
    implementation(project(":insights:domain:contract"))
    implementation(project(":insights:domain:model"))
    implementation(project(":notification"))
    implementation(project(":notification:domain"))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime)
    implementation(libs.firebase.auth)
    implementation(libs.hilt.android)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.navigation.compose)
    implementation(libs.navigation.runtime)
    implementation(libs.timber)

    testImplementation(project(":user-info:domain:model"))
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk.core)
}
