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
    implementation(project(":core:common"))
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.room.paging)
    implementation(libs.room.runtime.android)

    testImplementation(libs.junit)

    ksp(libs.room.compiler)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}
