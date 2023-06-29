plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.d9tilov.android.goals_di"
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
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":goals:goals-data:goals-data-contract"))
    implementation(project(":goals:goals-data:goals-data-impl"))
    implementation(project(":goals:goals-domain:goals-domain-contract"))
    implementation(project(":goals:goals-domain:goals-domain-impl"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))

    implementation(libs.hilt)
    
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.room)
}
