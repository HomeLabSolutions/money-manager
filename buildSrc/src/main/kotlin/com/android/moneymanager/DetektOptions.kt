package com.android.moneymanager

import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.android.moneymanager.task.CheckUnusedDependenciesByImpact
import org.gradle.api.Project
import com.android.moneymanager.task.GradleFileValidationTask
import com.android.moneymanager.task.ModuleChangeDetectorTask
import org.gradle.api.Task
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import java.io.ByteArrayOutputStream
import java.io.File

object DetektOptions {

    private val whitelistModuleKeywords = setOf(
        "buildSrc",
        ".ci",
        ".py",
    )

    fun Project.applyDetektOptions() {
        tasks.register<GradleFileValidationTask>("gradleCheck")
        tasks.register<ModuleChangeDetectorTask>("moduleDetector")
        tasks.register<CheckUnusedDependenciesByImpact>("checkDependenciesByImpact")
        tasks.withType<CheckUnusedDependenciesByImpact>().configureEach {
            System.out.println("moggot allProjects: ${project.rootProject.allprojects}")
            val modules = getChangedModules()
            System.out.println("moggot modules: $modules")
            val tasks: List<Task?> = modules
                .map { module -> project.rootProject.allprojects.find { it.path == ":$module" }?.tasks?.findByName("projectHealth") }
            System.out.println("moggot tasks: $tasks")
            dependsOn(tasks)
        }
    }

    private fun Project.getChangedModules(): List<String> {
        val output = ByteArrayOutputStream()
        exec {
            commandLine = listOf("git", "diff", "--name-only", "origin/develop")
            standardOutput = output
        }
        val changedFiles = output.toString().trim().split("\n")
        return changedFiles
            .asSequence()
            .mapNotNull { file -> file.split("\n").firstOrNull() }
            .filter { s -> whitelistModuleKeywords.none { keyword -> s.contains(keyword, ignoreCase = true) } }
            .mapNotNull { findBuildFilePath(File(it)) }
            .map { it.replace("/",":").replace("build.gradle.kts", "").removeSuffix(":") }
            .distinct()
            .toList()
    }

    private fun findBuildFilePath(file: File): String? {
        var currentFile = file
        while (currentFile.parentFile != null) {
            currentFile = currentFile.parentFile
            val buildFile = File(currentFile, "build.gradle.kts")
            if (buildFile.exists()) {
                return buildFile.path
            }
        }
        return null
    }
}
