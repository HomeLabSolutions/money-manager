package com.android.moneymanager.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.ByteArrayOutputStream
import java.io.File

abstract class CheckUnusedDependenciesByImpact : DefaultTask() {
    private val whitelistModuleKeywords =
        setOf(
            "buildSrc",
            ".ci",
            ".py",
        )

    @TaskAction
    fun action() {
        println("moggot Executing CheckUnusedDependenciesByImpact task")
    }

    private fun getChangedModules(): List<String> {
        val output = ByteArrayOutputStream()
        project.exec {
            commandLine = listOf("git", "diff", "--name-only", "origin/develop")
            standardOutput = output
        }
        val changedFiles = output.toString().trim().split("\n")
        return changedFiles
            .asSequence()
            .mapNotNull { file -> file.split("\n").firstOrNull() }
            .filter { s -> whitelistModuleKeywords.none { keyword -> s.contains(keyword, ignoreCase = true) } }
            .mapNotNull { findBuildFilePath(File(it)) }
            .map { it.replace("/", ":").replace("build.gradle.kts", "").removeSuffix(":") }
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
