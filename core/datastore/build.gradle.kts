plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.datastore"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.datastore)
    implementation(libs.hilt.android)
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
