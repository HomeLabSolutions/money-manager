package com.android.moneymanager.gradle.plugins

import androidx.baselineprofile.gradle.consumer.BaselineProfileConsumerExtension
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.moneymanager.gradle.extensions.buildLibs
import com.google.gms.googleservices.GoogleServicesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidApplicationBaselineProfileConventionPlugin : Plugin<Project> {
    private val baselineProfileBuildTypes = listOf("nonMinifiedRelease", "benchmarkRelease")

    override fun apply(target: Project) {
        val androidComponents = target.extensions.getByType<ApplicationAndroidComponentsExtension>()
        if (target.providers.gradleProperty("baselineProfile").orNull == "true") {
            target.pluginManager.apply(
                target.buildLibs.plugins.androidx.baselineprofile
                    .get()
                    .pluginId,
            )
            configureBuildTypes(target.extensions.getByType<ApplicationExtension>())
            configureFixtureSources(target, androidComponents)
            configureConsumer(target)
            configureFixtureDependencies(target)
            configureFirebase(target, androidComponents)
        } else {
            androidComponents.onVariants { variant ->
                if (!variant.debuggable) {
                    variant.sources.baselineProfiles?.addStaticSourceDirectory("src/main/generated/baselineProfiles")
                }
            }
        }
    }

    private fun configureBuildTypes(android: ApplicationExtension) {
        baselineProfileBuildTypes.forEach { buildTypeName ->
            android.buildTypes.maybeCreate(buildTypeName).apply {
                initWith(android.buildTypes.getByName("release"))
                applicationIdSuffix = ".baselineprofile"
                versionNameSuffix = "-baselineprofile"
                signingConfig = android.signingConfigs.getByName("debug")
                manifestPlaceholders["appName"] = "Money Manager profile"
            }
        }
    }

    private fun configureFixtureSources(
        target: Project,
        androidComponents: ApplicationAndroidComponentsExtension,
    ) {
        val fixtureManifest = target.rootProject.file("baselineprofile/target/AndroidManifest.xml")
        val fixtureSources = target.rootProject.file("baselineprofile/target/src")
        androidComponents.finalizeDsl { android ->
            baselineProfileBuildTypes.forEach { buildTypeName ->
                android.sourceSets
                    .getByName(buildTypeName)
                    .manifest
                    .srcFile(fixtureManifest)
            }
        }
        val compilationTaskNames =
            baselineProfileBuildTypes.map { "compile${it.replaceFirstChar(Char::uppercaseChar)}Kotlin" }
        target.tasks.withType<KotlinCompile>().configureEach {
            if (name in compilationTaskNames) {
                source(fixtureSources)
            }
        }
    }

    private fun configureConsumer(target: Project) {
        target.extensions.configure<BaselineProfileConsumerExtension> {
            automaticGenerationDuringBuild = false
            saveInSrc = true
            mergeIntoMain = true
        }
    }

    private fun configureFixtureDependencies(target: Project) {
        target.dependencies.add("baselineProfile", target.project(":baselineprofile"))
        baselineProfileBuildTypes.forEach { buildType ->
            target.dependencies.add("${buildType}Implementation", target.project(":core:database"))
            target.dependencies.add("${buildType}Implementation", target.buildLibs.room.runtime.android)
        }
    }

    private fun configureFirebase(
        target: Project,
        androidComponents: ApplicationAndroidComponentsExtension,
    ) {
        val firebaseConfiguration = target.rootProject.file("baselineprofile/google-services.json")
        androidComponents.onVariants { variant ->
            if (variant.buildType in baselineProfileBuildTypes) {
                val variantName = variant.name.replaceFirstChar(Char::uppercaseChar)
                target.tasks.named<GoogleServicesTask>("process${variantName}GoogleServices") {
                    googleServicesJsonFiles.set(listOf(firebaseConfiguration))
                }
            }
        }
    }
}
