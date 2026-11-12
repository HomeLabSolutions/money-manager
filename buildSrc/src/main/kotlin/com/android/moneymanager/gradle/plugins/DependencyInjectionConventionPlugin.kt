package com.android.moneymanager.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class DependencyInjectionConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("dev.zacsweers.metro")
    }
}
