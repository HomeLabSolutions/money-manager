package com.android.moneymanager.gradle

import org.gradle.api.Project

object DependencySortingOptions {
    fun Project.applyDependencySortingOptions() {
        allprojects {
            pluginManager.apply("com.squareup.sort-dependencies")
        }

        tasks.named("check") {
            dependsOn(subprojects.map { it.tasks.matching { task -> task.name == "checkSortDependencies" } })
        }
    }
}
