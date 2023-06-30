plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.billing_data_impl"
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

    implementation(project(":core:common"))
    implementation(project(":billing:billing-data:billing-data-contract"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))
    implementation(project(":billing:billing-domain:billing-domain-model"))

    implementation(project(":currency:currency-domain:currency-domain-model"))

    implementation(libs.coroutinesCore)
    implementation (libs.timber)

}
