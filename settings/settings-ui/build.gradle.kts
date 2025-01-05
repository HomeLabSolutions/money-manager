plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    id("kotlin-parcelize")
}

android {
    namespace = "com.d9tilov.android.settings_ui"

    defaultConfig {

        vectorDrawables.useSupportLibrary = true
    }
}

dependencies {

    implementation(project(":backup:backup-domain:backup-domain-contract"))
    implementation(project(":backup:backup-domain:backup-domain-model"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))
    implementation(project(":billing:billing-domain:billing-domain-model"))
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:network"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":user-info:user-domain:user-domain-contract"))
    implementation(project(":user-info:user-domain:user-domain-model"))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.firebase.config)
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
