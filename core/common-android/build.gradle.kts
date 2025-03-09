plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.library.compose")
    id("moneymanager.android.hilt")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.d9tilov.android.common.android"
}

dependencies {
    api(libs.appcompat)

    implementation(project(":core:common"))
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.core)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.material)
    implementation(libs.navigation.common)
    implementation(libs.navigation.runtime)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.work.ktx)

    ksp(libs.hilt.ext.compiler)
}
