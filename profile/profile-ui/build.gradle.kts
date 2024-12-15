plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    id("com.autonomousapps.dependency-analysis")
}

android {
    namespace = "com.d9tilov.android.profile_ui"
}

dependencies {


    implementation(project(":backup:backup-data:backup-data-impl"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))
    implementation(project(":budget:budget-domain:budget-domain-model"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))
    implementation(project(":user-info:user-domain:user-domain-contract"))
    implementation(project(":user-info:user-domain:user-domain-model"))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.appcompat)
    implementation(libs.coil)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.ui.auth)
    implementation(libs.play.services.auth)
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
