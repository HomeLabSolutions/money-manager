plugins {
    `kotlin-dsl`
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
    }
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.android.gradle.plugin)
    implementation(libs.androidx.baselineprofile.gradle.plugin)
    implementation(libs.detekt)
    implementation(libs.google.services)
    implementation(libs.java.poet)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.metadata.jvm)
    implementation(libs.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "moneymanager.android.application"
            implementationClass = "com.android.moneymanager.gradle.plugins.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "moneymanager.android.library"
            implementationClass = "com.android.moneymanager.gradle.plugins.AndroidLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "moneymanager.android.hilt"
            implementationClass = "com.android.moneymanager.gradle.plugins.AndroidHiltConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "moneymanager.android.application.compose"
            implementationClass = "com.android.moneymanager.gradle.plugins.AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplicationBaselineProfile") {
            id = "moneymanager.android.application.baseline-profile"
            implementationClass =
                "com.android.moneymanager.gradle.plugins.AndroidApplicationBaselineProfileConventionPlugin"
        }
        register("androidApplicationStartupBenchmark") {
            id = "moneymanager.android.application.startup-benchmark"
            implementationClass =
                "com.android.moneymanager.gradle.plugins.AndroidApplicationStartupBenchmarkConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "moneymanager.android.library.compose"
            implementationClass = "com.android.moneymanager.gradle.plugins.AndroidLibraryComposeConventionPlugin"
        }
        register("kotlinLibrary") {
            id = "moneymanager.android.library.kotlin"
            implementationClass = "com.android.moneymanager.gradle.plugins.KotlinLibraryConventionPlugin"
        }
    }
}
