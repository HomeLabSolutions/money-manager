plugins {
    `kotlin-dsl`
}

buildscript {
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
    }
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.ksp.gradle.plugin)
    implementation(libs.java.poet)
    implementation(libs.unused.deps)
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
        register("kotlinLibrary") {
            id = "moneymanager.android.library.kotlin"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
    }
}
