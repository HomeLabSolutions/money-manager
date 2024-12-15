plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":core:common"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))
    implementation(libs.kotlinx.coroutines.core)
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
