plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":backup:backup-domain:backup-domain-contract"))
    implementation(project(":backup:backup-domain:backup-domain-model"))
    implementation(project(":core:common"))
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
