plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.billing_di"
}

dependencies {

    implementation(project(":billing:billing-data:billing-data-contract"))
    implementation(project(":billing:billing-data:billing-data-impl"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))
    implementation(project(":billing:billing-domain:billing-domain-impl"))
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
