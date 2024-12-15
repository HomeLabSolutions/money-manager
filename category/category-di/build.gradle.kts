plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.category_di"
}

dependencies {

    implementation(project(":category:category-data:category-data-contract"))
    implementation(project(":category:category-data:category-data-impl"))
    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":category:category-domain:category-domain-impl"))

    implementation(libs.hilt.android)
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
