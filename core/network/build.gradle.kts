import java.io.FileInputStream
import java.util.Properties

plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    alias(libs.plugins.serialization)
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
val apiKey =
    if (keystorePropertiesFile.exists()) {
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
        keystoreProperties["currency_api_key"] as String
    } else {
        println("Warning: keystore.properties file not found. Release signing configuration will not be applied.")
        ""
    }

android {
    namespace = "com.d9tilov.android.network"

    defaultConfig {
        buildConfigField("String", "CURRENCY_API_KEY", "\"$apiKey\"")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.timber)
}
