import java.io.FileInputStream
import java.util.Properties

plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
var currencyApiKey = ""
var geocodingApiKey = ""
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
    currencyApiKey = keystoreProperties["currency_api_key"] as String
    geocodingApiKey = keystoreProperties["geocoding_api_key"] as String
} else {
    println("Warning: keystore.properties file not found. API keys will not be configured.")
}

android {
    namespace = "com.d9tilov.android.currency.data.impl"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "CURRENCY_API_KEY", "\"$currencyApiKey\"")
        buildConfigField("String", "GEOCODING_API_KEY", "\"$geocodingApiKey\"")
    }

    lint {
        disable.apply {
            add("CoroutineCreationDuringComposition")
            add("FlowOperatorInvokedInComposition")
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":currency:data:contract"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:model"))
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime)
    implementation(libs.dagger)
    implementation(libs.hilt.common)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.retrofit)
    implementation(libs.startup)
    implementation(libs.timber)
}
