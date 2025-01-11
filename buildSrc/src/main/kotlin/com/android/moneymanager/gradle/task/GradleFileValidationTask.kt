package com.android.moneymanager.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.pathString

abstract class GradleFileValidationTask : DefaultTask() {
    private val ignoredPaths =
        setOf(
            "gradle-plugins",
            "dfm_host_flutter",
            "-app/build.gradle.kts",
            "buildSrc",
            "taga/debugger/build.gradle.kts",
            "kontrakt-android/build.gradle.kts",
            "trip/demoapp/build.gradle.kts",
            "trip/trip-building/trip-building-app-di/build.gradle.kts",
            "cart/cart-app-di/build.gradle.kts",
            "performance/benchmark/build.gradle.kts",
            "legacy-map/baidumap/build.gradle.kts",
            "legacy-map/navermap/build.gradle.kts",
        )

    @TaskAction
    fun action() {
        val rootPath: Path = Paths.get(project.rootDir.path)
        Files.walkFileTree(
            rootPath,
            object : SimpleFileVisitor<Path>() {
                override fun visitFile(
                    file: Path,
                    attrs: BasicFileAttributes,
                ): FileVisitResult {
                    if (file.extension == "kts") {
                        if (shouldSkipFile(file, rootPath)) return FileVisitResult.CONTINUE
                        checkTransitiveInternalDependency(file)
                        checkUnusedDependencies(file)
                    }
                    return FileVisitResult.CONTINUE
                }

                override fun preVisitDirectory(
                    dir: Path,
                    attrs: BasicFileAttributes,
                ): FileVisitResult =
                    if (dir.name == "build" || dir.name == ".gradle") {
                        FileVisitResult.SKIP_SUBTREE
                    } else {
                        FileVisitResult.CONTINUE
                    }
            },
        )
    }

    private fun shouldSkipFile(
        path: Path,
        rootPath: Path,
    ): Boolean =
        if (path.parent == rootPath) {
            true
        } else {
            Files.lines(path).use { lines ->
                lines
                    .map { it.trim() }
                    .anyMatch { it.contains(TEST_APP_ANCHOR) or it.contains(APP_ANCHOR) }
            }
        }

    private fun checkTransitiveInternalDependency(path: Path) {
        Files.lines(path).use { lines ->
            lines
                .map { it.trim() }
                .filter { trimmedLine -> TRANSITIVE_DEPENDENCY_LIST.any { trimmedLine.startsWith(it) } }
                .forEach {
                    throw IllegalStateException(
                        "Transitive internal dependency $it\nin file $path is not allowed. Please, replace it with implementation",
                    )
                }
        }
    }

    private fun checkUnusedDependencies(path: Path) {
        val shouldIgnore = ignoredPaths.any { ignore -> path.pathString.contains(ignore) }
        if (shouldIgnore) return
        val found: Boolean =
            Files.lines(path).use { lines ->
                lines
                    .map { it.trim() }
                    .anyMatch { it.contains(DEPENDENCY_ANALYSIS_PREFIX) }
            }
        if (!found) {
            throw IllegalStateException(
                "Please check file $path\nfor unused dependencies with script [python3 unused_deps.py <file-path>]",
            )
        }
    }

    private companion object {
        const val DEPENDENCY_ANALYSIS_PREFIX = "dependencyAnalysis"
        const val TEST_APP_ANCHOR = "android-test-application"
        const val APP_ANCHOR = "com.android.application"
        val TRANSITIVE_DEPENDENCY_LIST =
            listOf(
                "api(project(\"",
                "debugApi(project(\"",
                "releaseApi(project(\"",
                "testApi(project(\"",
                "testDebugApi(project(\"",
                "testReleaseApi(project(\"",
            )
    }
}
