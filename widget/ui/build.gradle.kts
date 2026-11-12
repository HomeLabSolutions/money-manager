plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.library.compose")
    id("moneymanager.di")
}

android {
    namespace = "com.d9tilov.android.widget.ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":currency:domain:model"))
    implementation(project(":transaction:domain:model"))
    implementation(project(":widget:domain:contract"))
    implementation(project(":widget:domain:model"))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    implementation(libs.kotlinx.coroutines.core)

    androidTestImplementation(libs.androidx.core)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
