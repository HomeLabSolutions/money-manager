plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.transaction_ui"
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
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))
    implementation(project(":category:category-data:category-data-model"))
    implementation(project(":category:category-data:category-data-model"))
    implementation(project(":category:category-ui"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":transaction:transaction-data:transaction-data-model"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))
    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))

    implementation(libs.bundles.navigation)

    implementation(libs.firebaseAnalytics)
    implementation(platform(libs.firebaseBom))

    implementation(libs.appCompat)
    implementation(libs.material)

    implementation(libs.hilt)
    kapt(libs.hiltCompiler)
    kapt(libs.hiltAndroidCompiler)
}
