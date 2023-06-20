plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.d9tilov.android.billing_data_contract"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra

    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
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

    implementation(project(":billing:billing-data:billing-data-model"))

    implementation(libs.coroutinesCore)
    implementation(libs.billing)
}
