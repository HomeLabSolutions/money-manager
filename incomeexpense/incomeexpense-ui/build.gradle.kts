plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    
    
}

android {
    namespace = "com.d9tilov.android.incomeexpense_ui"

    defaultConfig {
        vectorDrawables.useSupportLibrary = true
    }
}


dependencies {
    
    implementation(project(":billing:billing-domain:billing-domain-contract"))
    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))
    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.appcompat)
    implementation(libs.firebase.analytics)
    implementation(libs.material)
    implementation(libs.navigation.compose)
    implementation(libs.paging.compose)
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
