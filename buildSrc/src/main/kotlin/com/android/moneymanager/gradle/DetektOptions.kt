package com.android.moneymanager.gradle

import com.android.moneymanager.gradle.task.TomlFileValidationTask
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.gradle.api.Project
import org.gradle.api.file.ProjectLayout
import org.gradle.api.tasks.SourceTask
import org.gradle.kotlin.dsl.register

object DetektOptions {
    private const val CONFIG_FILE = "detekt.yml"
    private const val BASELINE_FILE = "baseline.xml"

    fun Project.applyDetektOptions() {
        plugins.apply("io.gitlab.arturbosch.detekt")

        tasks.register<TomlFileValidationTask>("tomlCheck")

        tasks.register<Detekt>("detektCheck") {
            configureCommon()
            val configFolder = project.layout.projectDirectory.dir("config/detekt")
            config.setFrom(configFolder.file(CONFIG_FILE))
            baseline.set(configFolder.file(BASELINE_FILE))
        }

        tasks.register<Detekt>("detektCheckStrict") {
            configureCommon()
            buildUponDefaultConfig = true
        }

        tasks.register<DetektCreateBaselineTask>("detektCreateBaseline") {
            val configFolder = project.layout.projectDirectory.dir("config/detekt")
            config.setFrom(configFolder.file(CONFIG_FILE))
            baseline.set(configFolder.file(BASELINE_FILE))

            configureSources(project.layout)

            parallel.set(true)
        }
    }

    private fun SourceTask.configureSources(layout: ProjectLayout) {
        setSource(layout.projectDirectory)

        include("**/*.kt")
        include("**/*.kts")

        exclude("**/analytics/impl/*")
        exclude("**/analytics/pojo/*")

        exclude("**/build/**")
        exclude("**/resources/**")

        // Exclude gradle cache directory in CI
        exclude("**/.gradle/**")
    }

    private fun Detekt.configureCommon() {
        // Use providers to avoid configuration cache issues
        basePath = project.projectDir.absolutePath // required for CI reporting

        configureSources(project.layout)

        parallel = true

        reports {
            html {
                required.set(true)
                outputLocation.set(
                    project.layout.buildDirectory.dir("reports/detekt").map { it.file("detekt-report.html") }
                )
            }

            // disable unwanted report formats
            sarif { required.set(false) }
            txt { required.set(false) }
            md { required.set(false) }
        }
    }
}
