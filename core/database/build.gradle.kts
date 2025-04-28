plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.d9tilov.android.database"

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
    api(libs.room.runtime.android)

    implementation(project(":core:common"))
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.room.paging)

    ksp(libs.room.compiler)
}
