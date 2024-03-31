// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(libs.googlePlayServicesPlugin)
        classpath(libs.navigationArgsPlugin)
        classpath(libs.hiltPlugin)
        classpath(libs.firebaseCrashlyticsPlgin)
        classpath(libs.detektPlugin)
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

repositories {
    mavenCentral()
}

extra["compileSdkVersion"] = 34
extra["minSdkVersion"] = 21
extra["targetSdkVersion"] = 34
extra["versionMajor"] = 1
extra["versionMinor"] = 0
extra["versionPatch"] = 5
extra["versionBuild"] = 1

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

plugins {
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.deps) apply true // ./gradlew buildHealth
}
