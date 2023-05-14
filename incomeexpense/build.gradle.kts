plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.incomeexpense"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion

        vectorDrawables.useSupportLibrary = true
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
    implementation(project(":core:designsystem"))
    implementation(project(":core:common-android"))
    implementation(project(":category:category-data:category-data-model"))
    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":category:category-ui"))
    implementation(project(":currency:currency-data:currency-data-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))
    implementation(project(":transaction:transaction-ui"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))

    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.paging)

    implementation(libs.bundles.navigation)

    implementation(libs.timber)

    implementation(libs.hilt)
    implementation(libs.hiltNavigationCompose)
    kapt(libs.hiltCompiler)
    kapt(libs.hiltAndroidCompiler)


    implementation(libs.firebaseAnalytics)
    implementation(platform(libs.firebaseBom))

}
