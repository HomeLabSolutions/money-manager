package com.android.moneymanager.gradle

import org.gradle.api.Project

object DependencySortingOptions {
    fun Project.applyDependencySortingOptions() {
        allprojects {
            pluginManager.apply("com.squareup.sort-dependencies")
        }

        tasks.named("check") {
            dependsOn(
                subprojects
                    .filter { "checkSortDependencies" in it.tasks.names }
                    .map { it.tasks.named("checkSortDependencies") },
            )
        }
    }
}
