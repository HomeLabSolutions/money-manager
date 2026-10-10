import com.android.moneymanager.gradle.DetektOptions.applyDetektOptions
import com.android.moneymanager.gradle.FormattingOptions.applyPrecheckOptions

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(libs.firebase.crashlytics.gradle)
        classpath(libs.google.services)
        classpath(libs.hilt.android.gradle.plugin)
    }
}

applyPrecheckOptions()
applyDetektOptions()

allprojects {
    apply(plugin = "com.squareup.sort-dependencies")

    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

repositories {
    mavenCentral()
}

extra["compileSdkVersion"] = 37
extra["minSdkVersion"] = 24
extra["targetSdkVersion"] = 36
extra["versionMajor"] = 1
extra["versionMinor"] = 3
extra["versionPatch"] = 7
extra["versionBuild"] = 1

tasks.named("clean", Delete::class) {
    description = "clean"
    delete(rootProject.layout.buildDirectory)
}

plugins {
    base
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.deps.sorting)
    alias(libs.plugins.deps.unused) apply true
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.secrets.gradle.plugin) apply false
}

tasks.named("check") {
    dependsOn(subprojects.map { it.tasks.matching { task -> task.name == "checkSortDependencies" } })
}

dependencyAnalysis {
    val fail = "fail"
    val ignore = "ignore"
    issues {
        all {
            onUnusedDependencies { severity(fail) }
            onUsedTransitiveDependencies { severity(ignore) }
            onIncorrectConfiguration { severity(ignore) }
            onCompileOnly { severity(ignore) }
            onRuntimeOnly { severity(ignore) }
            onUnusedAnnotationProcessors { severity(ignore) }
            onRedundantPlugins { severity(ignore) }
        }
    }
}
