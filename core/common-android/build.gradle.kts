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

    api(libs.fragment)
    api(libs.appCompat)
    implementation(libs.paging)
    implementation(libs.material)
    implementation(libs.lifecycleRuntime)

    implementation(libs.coroutinesCore)

    implementation(libs.glide)
    ksp(libs.glideCompiler)

    implementation(libs.core)

    implementation(libs.navigation)
    implementation(libs.navigationUi)

    implementation(libs.worker)
    implementation(libs.workerHilt)
    ksp(libs.workerHiltCompiler)

    implementation(libs.composeUi)
    
    implementation(libs.composeViewModel)
    implementation(libs.composeRuntime)
    implementation(libs.hiltNavigationCompose)
}
