plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.d9tilov.android.regular_transaction_domain_impl"
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
    implementation(project(":category:category-data:category-data-model"))
    implementation(project(":category:category-domain:category-domain-contract"))

    implementation(project(":currency:currency-data:currency-data-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))

    implementation(project(":transaction:regular-transaction-data:regular-transaction-data-model"))
    implementation(project(":transaction:regular-transaction-data:regular-transaction-data-contract"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))

    implementation(libs.coroutinesCore)
}
