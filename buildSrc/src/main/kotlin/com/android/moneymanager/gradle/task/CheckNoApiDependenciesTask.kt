package com.android.moneymanager.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class CheckNoApiDependenciesTask : DefaultTask() {
    @get:Input
    abstract val forbiddenDependencies: ListProperty<String>

    init {
        group = "verification"
        description = "Check that project configurations do not declare API dependencies"
        forbiddenDependencies.convention(emptyList())
        val rootProject = project.rootProject
        rootProject.allprojects.forEach { module ->
            if (module != rootProject) {
                rootProject.evaluationDependsOn(module.path)
            }
            module.configurations.matching { it.name == "api" || it.name.endsWith("Api") }.all {
                val configurationName = name
                dependencies.all {
                    forbiddenDependencies.add("${module.path}:$configurationName -> $this")
                }
                dependencyConstraints.all {
                    forbiddenDependencies.add("${module.path}:$configurationName constraint -> $this")
                }
            }
        }
    }

    @TaskAction
    fun checkDependencies() {
        val violations = forbiddenDependencies.get().distinct().sorted()
        if (violations.isNotEmpty()) {
            throw GradleException(
                "API dependencies are forbidden. Use implementation instead:\n${violations.joinToString("\n")}",
            )
        }
    }
}
