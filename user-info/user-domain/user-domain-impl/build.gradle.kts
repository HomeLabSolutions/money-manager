plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":user-info:user-domain:user-domain-contract"))
    implementation(project(":user-info:user-domain:user-domain-model"))
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
