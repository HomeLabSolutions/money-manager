import com.android.moneymanager.DetektOptions.applyDetektOptions
import com.dropbox.affectedmoduledetector.AffectedModuleConfiguration

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(libs.google.services)
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.firebase.crashlytics.gradle)
        classpath(libs.detekt.gradle.plugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

applyDetektOptions()

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

repositories {
    mavenCentral()
}

extra["compileSdkVersion"] = 34
extra["minSdkVersion"] = 23
extra["targetSdkVersion"] = 34
extra["versionMajor"] = 1
extra["versionMinor"] = 0
extra["versionPatch"] = 24
extra["versionBuild"] = 1

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

plugins {
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.deps) apply true // ./gradlew buildHealth
    alias(libs.plugins.deps.sorting) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.module.detector) apply true
}

affectedModuleDetector {
    baseDir = "${project.rootDir}"
    pathsAffectingAllModules = setOf()
    logFilename = "output.log"
    logFolder = "${project.rootDir}/output"
    compareFrom = "PreviousCommit" //default is PreviousCommit
    excludedModules = setOf()
    specifiedBranch = "develop"
    ignoredFiles = setOf()
    includeUncommitted = false
    top = "HEAD"
    customTasks = setOf(
        AffectedModuleConfiguration.CustomTask(
            "runDetektByImpact",
            "projectHealth",
            "Run projectHealth task for current module"
        )
    )
}

subprojects {
    apply(plugin = "com.squareup.sort-dependencies")
}

dependencyAnalysis {
    issues {
        all {
            onUnusedDependencies { severity("fail") }
            onUsedTransitiveDependencies { severity("ignore") }
            onIncorrectConfiguration { severity("ignore") }
            onCompileOnly { severity("ignore") }
            onRuntimeOnly { severity("ignore") }
            onUnusedAnnotationProcessors { severity("ignore") }
            onRedundantPlugins { severity("ignore") }
        }
    }
}
