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
                arguments += mapOf(
                    "room.incremental" to "true",
                    "room.schemaLocation" to "$projectDir/schemas"
                )
            }
        }
    }
}

dependencies {
    implementation(project(":core:common"))

    api(libs.room)
    api(libs.roomRuntime)
    implementation(libs.roomPaging)
    kapt(libs.roomCompiler)
    implementation(libs.coroutinesCore)
}
