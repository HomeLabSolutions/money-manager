import java.io.FileInputStream
import java.util.Properties

plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    alias(libs.plugins.serialization)
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
var currencyApiKey = ""
var geocodingApiKey = ""
var geoapifyApiKey = providers.environmentVariable("GEOAPIFY_API_KEY").orNull.orEmpty()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
    currencyApiKey = keystoreProperties["currency_api_key"] as String
    geocodingApiKey = keystoreProperties["geocoding_api_key"] as String
    geoapifyApiKey = keystoreProperties.getProperty("geoapify_api_key", geoapifyApiKey)
} else {
    println("Warning: keystore.properties file not found. Release signing configuration will not be applied.")
    ""
}

android {
    namespace = "com.d9tilov.android.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        listOf(
            "CURRENCY_API_KEY" to currencyApiKey,
            "GEOCODING_API_KEY" to geocodingApiKey,
            "GEOAPIFY_API_KEY" to geoapifyApiKey,
        ).forEach { (name, value) ->
            buildConfigField("String", name, "\"$value\"")
        }
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

    testImplementation(libs.junit)
}
