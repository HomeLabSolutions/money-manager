plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.splash_ui"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:network"))

    implementation(project(":backup:backup-domain:backup-domain-contract"))

    implementation(project(":user-info:user-domain:user-domain-model"))
    implementation(project(":user-info:user-data:user-data-impl"))
    implementation(project(":user-info:user-domain:user-domain-contract"))
    implementation(project(":user-info:user-di"))

    implementation(project(":category:category-domain:category-domain-contract"))

    implementation(libs.bundles.appCompat)
    implementation(libs.bundles.navigation)

    implementation(libs.hilt)
    
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.composeViewModel)

    implementation(libs.firebaseUi)
    implementation(libs.googlePlayServicesAuth)

    implementation(libs.timber)

    implementation(libs.splashScreen)
}
