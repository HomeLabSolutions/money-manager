// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.googlePlayServicesPlugin)
        classpath(libs.navigationArgsPlugin)
        classpath(libs.hiltPlugin)
        classpath(libs.firebaseCrashlyticsPlgin)
        classpath(libs.detektPlugin)
        classpath(libs.kotlinPlugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

extra["compileSdkVersion"] = 33
extra["minSdkVersion"] = 21
extra["targetSdkVersion"] = 33
extra["versionMajor"] = 1
extra["versionMinor"] = 0
extra["versionPatch"] = 3
extra["versionBuild"] = 5

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.ktlint) apply true
    alias(libs.plugins.deps) apply true // ./gradlew buildHealth
}
