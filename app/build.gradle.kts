import androidx.baselineprofile.gradle.consumer.BaselineProfileConsumerExtension
import com.google.gms.googleservices.GoogleServicesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("moneymanager.android.application")
    id("moneymanager.android.hilt")
    id("moneymanager.android.application.compose")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-parcelize")
    id("io.gitlab.arturbosch.detekt")
    alias(libs.plugins.secrets.gradle.plugin)
}

secrets {
    propertiesFileName = "keystore.properties"
    defaultPropertiesFileName = "local.defaults.properties"
    ignoreList.addAll(listOf("keyAlias", "keyPassword", "storeFile", "storePassword"))
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val releaseKeystorePath = providers.environmentVariable("ANDROID_KEYSTORE_PATH").orNull
val releaseBuildTypeName = "release"
val debugBuildTypeName = "debug"
val appNamePlaceholder = "appName"
val baselineProfileBuildTypes = listOf("nonMinifiedRelease", "benchmarkRelease")

android {

    signingConfigs {
        create(releaseBuildTypeName) {
            if (!releaseKeystorePath.isNullOrBlank()) {
                storeFile = file(releaseKeystorePath)
                storePassword = providers.environmentVariable("ANDROID_KEYSTORE_PASSWORD").get()
                keyAlias = providers.environmentVariable("ANDROID_KEY_ALIAS").get()
                keyPassword = providers.environmentVariable("ANDROID_KEY_PASSWORD").get()
            } else if (keystorePropertiesFile.exists()) {
                val keystoreProperties = Properties()
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"]!!)
                storePassword = keystoreProperties["storePassword"] as String
            } else {
                println(
                    "Warning: keystore.properties file not found. Release signing configuration will not be applied.",
                )
            }
        }
        getByName(debugBuildTypeName) {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    namespace = "com.d9tilov.moneymanager"

    defaultConfig {

        applicationId = "com.d9tilov.moneymanager"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            if (!releaseKeystorePath.isNullOrBlank() || keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName(releaseBuildTypeName)
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            manifestPlaceholders[appNamePlaceholder] = "Money Manager"
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
            isDebuggable = true
            manifestPlaceholders[appNamePlaceholder] = "Money Manager debug"
            signingConfig = signingConfigs.getByName(debugBuildTypeName)
        }
    }

    if (project.hasProperty("devBuild")) {
        splits.abi.isEnable = false
    }
}

dependencies {
    implementation(project(":analytics:di"))
    implementation(project(":analytics:domain"))
    implementation(project(":backup:data:impl"))
    implementation(project(":backup:di"))
    implementation(project(":backup:domain:contract"))
    implementation(project(":billing:di"))
    implementation(project(":billing:domain:contract"))
    implementation(project(":budget:di"))
    implementation(project(":budget:domain:contract"))
    implementation(project(":budget:domain:model"))
    implementation(project(":budget:ui"))
    implementation(project(":category:di"))
    implementation(project(":category:domain:contract"))
    implementation(project(":category:domain:model"))
    implementation(project(":category:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:network"))
    implementation(project(":currency:data:impl"))
    implementation(project(":currency:di"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:model"))
    implementation(project(":currency:observer:contract"))
    implementation(project(":currency:ui"))
    implementation(project(":incomeexpense:ui"))
    implementation(project(":insights:di"))
    implementation(project(":insights:presentation"))
    implementation(project(":notification"))
    implementation(project(":notification:domain"))
    implementation(project(":places:di"))
    implementation(project(":places:domain:model"))
    implementation(project(":profile:ui"))
    implementation(project(":settings:ui"))
    implementation(project(":statistics:ui"))
    implementation(project(":transaction:di"))
    implementation(project(":transaction:domain:contract"))
    implementation(project(":transaction:regular:data:impl"))
    implementation(project(":transaction:regular:di"))
    implementation(project(":transaction:regular:ui"))
    implementation(project(":transaction:ui"))
    implementation(project(":user-info:data:impl"))
    implementation(project(":user-info:di"))
    implementation(project(":user-info:domain:contract"))
    implementation(project(":user-info:domain:model"))
    implementation(project(":widget:di"))
    implementation(project(":widget:domain:contract"))
    implementation(project(":widget:ui"))
    implementation(libs.activity)
    implementation(libs.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.window.size)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime)
    implementation(libs.coil.core)
    implementation(libs.coil.network.cache.control)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.singleton)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.config)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.ui.auth)
    implementation(libs.googleid)
    implementation(libs.hilt.android)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.datetime)
    implementation(libs.material)
    implementation(libs.navigation.compose)
    implementation(libs.play.services.location)
    implementation(libs.timber)

    runtimeOnly(libs.androidx.credentials.play.services.auth)
    runtimeOnly(libs.profileinstaller)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk.core)

    androidTestImplementation(project(":backup:domain:model"))
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}

if (providers.gradleProperty("baselineProfile").orNull == "true") {
    apply(plugin = "androidx.baselineprofile")

    android.buildTypes {
        baselineProfileBuildTypes.forEach { buildTypeName ->
            maybeCreate(buildTypeName).apply {
                initWith(getByName(releaseBuildTypeName))
                applicationIdSuffix = ".baselineprofile"
                versionNameSuffix = "-baselineprofile"
                signingConfig = android.signingConfigs.getByName(debugBuildTypeName)
                manifestPlaceholders[appNamePlaceholder] = "Money Manager profile"
            }
        }
    }

    androidComponents.finalizeDsl {
        baselineProfileBuildTypes.forEach { buildTypeName ->
            android.sourceSets.getByName(buildTypeName).apply {
                manifest.srcFile(rootProject.file("baselineprofile/target/AndroidManifest.xml"))
            }
        }
    }
    tasks.withType<KotlinCompile>().configureEach {
        if (name in listOf("compileNonMinifiedReleaseKotlin", "compileBenchmarkReleaseKotlin")) {
            source(rootProject.file("baselineprofile/target/src"))
        }
    }

    configure<BaselineProfileConsumerExtension> {
        automaticGenerationDuringBuild = false
        saveInSrc = true
        mergeIntoMain = true
    }

    dependencies {
        add("baselineProfile", project(":baselineprofile"))
        listOf("nonMinifiedReleaseImplementation", "benchmarkReleaseImplementation").forEach { configuration ->
            add(configuration, project(":core:database"))
            add(configuration, libs.room.runtime.android)
        }
    }

    androidComponents.onVariants { variant ->
        if (variant.buildType in listOf("nonMinifiedRelease", "benchmarkRelease")) {
            val variantName = variant.name.replaceFirstChar(Char::uppercaseChar)
            tasks.named<GoogleServicesTask>("process${variantName}GoogleServices") {
                googleServicesJsonFiles.set(listOf(rootProject.file("baselineprofile/google-services.json")))
            }
        }
    }
} else {
    androidComponents.onVariants { variant ->
        if (!variant.debuggable) {
            variant.sources.baselineProfiles?.addStaticSourceDirectory("src/main/generated/baselineProfiles")
        }
    }
}
