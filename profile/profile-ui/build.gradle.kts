plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.profile_ui"
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
    implementation(project(":core:designsystem"))

    implementation(project(":user-info:user-domain:user-domain-model"))
    implementation(project(":user-info:user-domain:user-domain-contract"))
    implementation(project(":user-info:user-di"))

    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-ui"))

    implementation(project(":budget:budget-domain:budget-domain-model"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))
    implementation(project(":budget:budget-ui"))

    implementation(project(":billing:billing-domain:billing-domain-contract"))

    implementation(project(":category:category-domain:category-domain-model"))

    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))
    implementation(project(":transaction:regular-transaction-ui"))

    implementation(project(":analytics:analytics-di"))

    implementation(project(":backup:backup-ui"))
    implementation(project(":settings:settings-ui"))
    implementation(project(":goals:goals-ui"))
    implementation(project(":splash:splash-ui"))
    implementation(project(":incomeexpense:incomeexpense-ui"))

    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.constraintLayout)
    implementation(libs.composeConstraintLayout)
    implementation(libs.coil)

    implementation(libs.bundles.navigation)

    implementation(libs.bundles.composeMaterial3)
    implementation(libs.bundles.compose)
    implementation(libs.composeViewModel)
    implementation(libs.composeRuntime)

    implementation(libs.hilt)
    implementation(libs.hiltNavigationCompose)
    
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.firebaseAnalytics)
    implementation(platform(libs.firebaseBom))

    implementation(libs.firebaseUi)
    implementation(libs.googlePlayServicesAuth)
}
