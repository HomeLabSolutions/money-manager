import java.io.FileInputStream
import java.util.*

plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.d9tilov.android.network"

    defaultConfig {
        buildConfigField("String", "API_KEY", keystoreProperties["currency_api_key"] as String)
    }
}

dependencies {

    implementation(project(":core:common"))

    implementation(libs.retrofit)
    implementation(libs.retrofitMoshi)
    implementation(libs.okHttp)
    implementation(libs.okHttpInterceptor)

    implementation(libs.timber)
}
