plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.currency_di"
}

dependencies {
    implementation(project(":budget:budget-domain:budget-domain-contract"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":currency:currency-data:currency-data-contract"))
    implementation(project(":currency:currency-data:currency-data-impl"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-domain:currency-domain-impl"))
    implementation(project(":currency:currency-observer:currency-observer-contract"))
    implementation(project(":currency:currency-observer:currency-observer-impl"))
    implementation(libs.retrofit)
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
