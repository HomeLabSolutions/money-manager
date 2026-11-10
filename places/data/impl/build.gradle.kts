import java.io.FileInputStream
import java.util.Properties

plugins {
    id("moneymanager.android.library")
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
var geoapifyApiKey = ""
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
    geoapifyApiKey = keystoreProperties["geoapify_api_key"] as String
} else {
    println("Warning: keystore.properties file not found. API keys will not be configured.")
}

android {
    namespace = "com.d9tilov.android.places.data.impl"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "GEOAPIFY_API_KEY", "\"$geoapifyApiKey\"")
    }
}

dependencies {
    implementation(project(":places:data:contract"))
    implementation(project(":places:domain:contract"))
    implementation(project(":places:domain:model"))
    implementation(libs.javax.inject)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.http)

    testImplementation(project(":core:network"))
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(libs.ktor.client.mock)
}
