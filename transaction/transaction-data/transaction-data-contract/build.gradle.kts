plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.transaction_data_contract"

    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments +=
                    mapOf(
                        "room.incremental" to "true",
                        "room.schemaLocation" to "$projectDir/schemas",
                    )
            }
        }
    }
}

dependencies {
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":core:common"))
    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.paging.common)
}
