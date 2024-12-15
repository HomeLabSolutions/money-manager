package com.android.moneymanager.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.BufferedReader
import java.io.InputStreamReader

abstract class ModuleChangeDetectorTask : DefaultTask() {

    @TaskAction
    fun action() {
        try {
            val process = ProcessBuilder("git", "diff", "--name-only", "develop")
                .redirectErrorStream(true)
                .start()

            val output = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }
            val exitCode = process.waitFor()

            if (exitCode == 0) {
                println("Changed files compared to develop branch:")
                println(output)
            } else {
                println("Error occurred while fetching changed files:")
                println(output)
            }
        } catch (e: Exception) {
            println("An error occurred: ${e.message}")
        }
    }
}

