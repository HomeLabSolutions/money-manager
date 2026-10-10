import com.android.moneymanager.gradle.extensions.ksp

plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.library.compose")
    id("moneymanager.android.hilt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.d9tilov.android.common.android"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.accompanist.permissions)
    implementation(libs.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.lifecycle.viewmodel.compose.android)
    implementation(libs.androidx.work.runtime)
    implementation(libs.appcompat)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.datetime)
    implementation(libs.navigation.common)
    implementation(libs.navigation.runtime)
    implementation(libs.play.services.location)
    implementation(libs.timber)

    ksp(libs.hilt.ext.compiler)
}
