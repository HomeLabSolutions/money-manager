package com.android.moneymanager.gradle

import com.android.moneymanager.gradle.extensions.buildLibs
import org.gradle.api.Project

object DependencySortingOptions {
    fun Project.applyDependencySortingOptions() {
        val sortingPluginId =
            buildLibs.plugins.deps.sorting
                .get()
                .pluginId
        allprojects {
            pluginManager.apply(sortingPluginId)
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
