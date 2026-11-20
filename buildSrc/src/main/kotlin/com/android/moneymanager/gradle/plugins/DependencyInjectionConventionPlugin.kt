package com.android.moneymanager.gradle.plugins

import com.android.moneymanager.gradle.extensions.buildLibs
import org.gradle.api.Plugin
import org.gradle.api.Project

class DependencyInjectionConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply(
            target.buildLibs.plugins.metro
                .get()
                .pluginId,
        )
    }
}
