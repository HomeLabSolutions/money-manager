plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.backup_ui"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))
    implementation(project(":backup:backup-domain:backup-domain-contract"))

    implementation(libs.bundles.navigation)

    implementation(libs.hilt)
    kapt(libs.hiltCompiler)
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.lifecycleViewModel)

    implementation(libs.bundles.worker)

    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseStorage)
    implementation(libs.firebaseConfig)

    implementation(libs.timber)
}

