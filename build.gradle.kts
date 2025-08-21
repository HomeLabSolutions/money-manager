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
        classpath(libs.google.services)
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.firebase.crashlytics.gradle)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

applyPrecheckOptions()
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

extra["compileSdkVersion"] = 36
extra["minSdkVersion"] = 23
extra["targetSdkVersion"] = 36
extra["versionMajor"] = 1
extra["versionMinor"] = 1
extra["versionPatch"] = 2
extra["versionBuild"] = 3

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

plugins {
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.deps.sorting) apply false
    alias(libs.plugins.deps.unused) apply true
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.serialization) apply false
}

subprojects {
    apply(plugin = "com.squareup.sort-dependencies")
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
