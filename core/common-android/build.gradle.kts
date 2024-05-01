plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.library.compose")
    id("moneymanager.android.library.viewbinding")
    id("moneymanager.android.hilt")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.d9tilov.android.common.android"
}

dependencies {

    implementation(project(":core:common"))

    api(libs.appcompat)
    implementation(libs.paging.runtime)
    implementation(libs.material)
    implementation(libs.lifecycle.runtime)

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.glide)
    ksp(libs.glide.compiler)

    implementation(libs.core)

    
    implementation(libs.navigation.ui)

    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)
    ksp(libs.hilt.android.compiler)

    implementation(libs.androidx.compose.ui)
    
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.hilt.navigation.compose)
}
