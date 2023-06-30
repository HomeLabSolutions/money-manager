plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.category_ui"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
    }

    buildFeatures {
        viewBinding = true
    }

    dataBinding {
        isEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))

    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":category:category-data:category-data-impl"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":category:category-di"))

    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))
    implementation(project(":transaction:transaction-di"))

    implementation(project(":transaction:regular-transaction-di"))

    implementation(project(":billing:billing-domain:billing-domain-contract"))

    implementation(project(":analytics:analytics-di"))

    implementation(libs.glide)
    kapt(libs.glideCompiler)

    implementation(libs.navigation)
    implementation(libs.navigationUi)

    implementation(libs.material)
    implementation(libs.constraintLayout)

    implementation(libs.hilt)
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.firebaseAnalytics)
    implementation(platform(libs.firebaseBom))
}
