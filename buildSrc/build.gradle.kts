import org.gradle.kotlin.dsl.`kotlin-dsl`
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

buildscript {
    dependencies {
        classpath(libs.kotlinPlugin)
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlinPlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.java.poet)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "moneymanager.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "moneymanager.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "moneymanager.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "moneymanager.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "moneymanager.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibraryViewBinding") {
            id = "moneymanager.android.library.viewbinding"
            implementationClass = "AndroidViewBindingConventionPlugin"
        }
    }
}
