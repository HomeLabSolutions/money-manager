plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.settings_ui"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    dataBinding {
        isEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))

    implementation(project(":billing:billing-data:billing-data-model"))
    implementation(project(":billing:billing-domain:billing-domain-model"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))

    implementation(project(":backup:backup-domain:backup-domain-model"))
    implementation(project(":backup:backup-domain:backup-domain-contract"))

    implementation(project(":user-info:user-data:user-data-model"))
    implementation(project(":user-info:user-domain:user-domain-contract"))
    
    implementation(project(":currency:currency-domain:currency-domain-model"))

    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.constraintLayout)
    implementation(libs.bundles.navigation)

    implementation(libs.bundles.compose)
    implementation(libs.bundles.composeMaterial3)
    implementation(libs.composeViewModel)
    implementation(libs.composeRuntime)

    implementation(libs.timber)

    implementation(libs.hilt)
    implementation(libs.hiltNavigationCompose)
    kapt(libs.hiltCompiler)
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.glide)
    kapt(libs.glideCompiler)

    implementation(libs.billing)

    implementation(libs.dotsIndicator)

    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseConfig)
}

