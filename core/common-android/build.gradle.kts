plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.library.viewbinding")
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.common.android"
}

dependencies {

    implementation(project(":core:common"))

    api(libs.fragment)
    api(libs.appCompat)
    implementation(libs.paging)
    implementation(libs.material)
    implementation(libs.lifecycleRuntime)

    implementation(libs.coroutinesCore)

    implementation(libs.glide)
    kapt(libs.glideCompiler)

    implementation(libs.core)

    implementation(libs.navigation)
    implementation(libs.navigationUi)
}
