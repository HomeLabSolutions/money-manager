import java.io.FileInputStream
import java.util.Properties

plugins {
    id("moneymanager.android.application")
    id("moneymanager.android.hilt")
    id("moneymanager.android.application.compose")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("io.gitlab.arturbosch.detekt")
}

android {

    signingConfigs {
        create("release") {
            val keystorePropertiesFile: File = rootProject.file("keystore.properties")
            val keystoreProperties = Properties()
            if (keystorePropertiesFile.exists()) {
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"]!!)
                storePassword = keystoreProperties["storePassword"] as String
            } else {
                println("Warning: keystore.properties file not found. Release signing configuration will not be applied.")
            }
        }
    }

    namespace = "com.d9tilov.moneymanager"

    defaultConfig {

        applicationId = "com.d9tilov.moneymanager"
        val versionMajor: Int by rootProject.extra
        val versionMinor: Int by rootProject.extra
        val versionPatch: Int by rootProject.extra
        val versionBuild: Int by rootProject.extra
        versionCode = 1000 * (1000 * versionMajor + 100 * versionMinor + versionPatch) + versionBuild
        versionName = "$versionMajor.$versionMinor.$versionPatch.$versionBuild"

        vectorDrawables.useSupportLibrary = true

        javaCompileOptions {
            annotationProcessorOptions {
                arguments +=
                    mapOf(
                        "room.incremental" to "true",
                        "room.schemaLocation" to "$projectDir/schemas",
                    )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            manifestPlaceholders["appName"] = "Money Manager"
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
            isDebuggable = true
            manifestPlaceholders["appName"] = "Money Manager debug"
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    configure<com.android.build.gradle.BaseExtension> {
        packagingOptions {
            exclude("META-INF/DEPENDENCIES")
            exclude("META-INF/LICENSE")
            exclude("META-INF/LICENSE.txt")
            exclude("META-INF/license.txt")
            exclude("META-INF/NOTICE")
            exclude("META-INF/NOTICE.txt")
            exclude("META-INF/notice.txt")
            exclude("META-INF/ASL2.0")
        }
    }

    if (project.hasProperty("devBuild")) {
        splits.abi.isEnable = false
        splits.density.isEnable = false
        aaptOptions.cruncherEnabled = false
    }

    tasks.getByPath("detekt").onlyIf { gradle.startParameter.taskNames.contains("detekt") }
}

dependencies {
    implementation(project(":analytics:analytics-di"))
    implementation(project(":backup:backup-data:backup-data-impl"))
    implementation(project(":backup:backup-di"))
    implementation(project(":backup:backup-domain:backup-domain-contract"))
    implementation(project(":billing:billing-di"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))
    implementation(project(":budget:budget-di"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))
    implementation(project(":budget:budget-domain:budget-domain-model"))
    implementation(project(":budget:budget-ui"))
    implementation(project(":category:category-di"))
    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":category:category-ui"))
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:network"))
    implementation(project(":currency:currency-data:currency-data-impl"))
    implementation(project(":currency:currency-di"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-observer:currency-observer-contract"))
    implementation(project(":currency:currency-ui"))
    implementation(project(":incomeexpense:incomeexpense-ui"))
    implementation(project(":profile:profile-ui"))
    implementation(project(":settings:settings-ui"))
    implementation(project(":statistics:statistics-ui"))
    implementation(project(":transaction:regular-transaction-di"))
    implementation(project(":transaction:regular-transaction-ui"))
    implementation(project(":transaction:transaction-di"))
    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))
    implementation(project(":transaction:transaction-domain:transaction-domain-model"))
    implementation(project(":transaction:transaction-ui"))
    implementation(project(":user-info:user-data:user-data-impl"))
    implementation(project(":user-info:user-di"))
    implementation(project(":user-info:user-domain:user-domain-contract"))
    implementation(project(":user-info:user-domain:user-domain-model"))
    implementation(libs.activity)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.window.size)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.work.runtime)
    implementation(libs.appcompat)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.config)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.ui.auth)
    implementation(libs.hilt.android)
    implementation(libs.material)
    implementation(libs.play.services.auth)
    implementation(libs.timber)
}
