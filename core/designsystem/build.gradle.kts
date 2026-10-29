plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.library.compose")
}

android {
    namespace = "com.d9tilov.android.designsystem"

    defaultConfig {
        vectorDrawables.useSupportLibrary = true
    }
}

dependencies {
    constraints {
        implementation(libs.androidx.lifecycle.runtime) {
            because("Older Lifecycle lint checks reference Kotlin APIs removed in AGP 9.5")
        }
    }

    implementation(project(":core:common"))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.kotlinx.datetime)

    debugImplementation(libs.androidx.compose.ui.tooling)
}
