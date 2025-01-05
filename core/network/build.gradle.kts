import java.io.FileInputStream
import java.util.*

plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

val apiKey: String = project.findProperty("currency_api_key") as String? ?: ""

android {
    namespace = "com.d9tilov.android.network"

    defaultConfig {
        buildConfigField("String", "CURRENCY_API_KEY", "\"$apiKey\"")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.timber)
}

dependencyAnalysis {
    val fail = "fail"
    val ignore = "ignore"
    issues {
        onUnusedDependencies {
            severity(fail)
            exclude(
                "",
            )
        }
        onUsedTransitiveDependencies { severity(ignore) }
        onIncorrectConfiguration { severity(ignore) }
        onCompileOnly { severity(ignore) }
        onRuntimeOnly { severity(ignore) }
        onUnusedAnnotationProcessors { severity(ignore) }
        onRedundantPlugins { severity(ignore) }
    }
}
