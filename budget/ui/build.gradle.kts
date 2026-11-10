plugins {
    alias(libs.plugins.koin.compiler)
    id("moneymanager.android.library")
    id("moneymanager.android.library.compose")
}

android {
    namespace = "com.d9tilov.android.budget.ui"
}

dependencies {
    implementation(project(":analytics:domain"))
    implementation(project(":budget:domain:contract"))
    implementation(project(":budget:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.core)
    implementation(libs.koin.core.viewmodel)
    implementation(libs.navigation.compose)
    implementation(libs.navigation.runtime)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.datetime)
    testImplementation(libs.mockk.core)
}
