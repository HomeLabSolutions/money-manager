import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
    id("io.gitlab.arturbosch.detekt")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"]!!)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra
    val targetSdkVersion: Int by rootProject.extra
    compileSdk = compileSdkVersion
    buildToolsVersion = "33.0.0"

    defaultConfig {
        val versionMajor = 1
        val versionMinor = 0
        val versionPatch = 3
        val versionBuild = 5

        applicationId = "com.d9tilov.moneymanager"
        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
        versionCode = 1000 * (1000 * versionMajor + 100 * versionMinor + versionPatch) + versionBuild
        versionName = "${versionMajor}.${versionMinor}.${versionPatch}.${versionBuild}"

        buildConfigField("String", "SALT", keystoreProperties["database_salt"] as String)
        buildConfigField(
            "String",
            "BASE64_ENCODED_PUBLIC_KEY",
            "\"" + keystoreProperties["base64EncodedPublicKey"] as String + "\""
        )

        vectorDrawables.useSupportLibrary = true

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.incremental" to "true",
                    "room.schemaLocation" to "$projectDir/schemas"
                )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["appName"] = "Money Manager"
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
            manifestPlaceholders["appName"] = "Money Manager debug"
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
    dataBinding {
        isEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    val ktlint by configurations.creating

    dependencies {
        ktlint(libs.ktlint) {
            attributes {
                attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
            }
        }
    }

    val outputDir = "${project.buildDir}/reports/ktlint/"
    val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

    val ktlintCheck by tasks.creating(JavaExec::class) {
        inputs.files(inputFiles)
        outputs.dir(outputDir)

        description = "Check Kotlin code style."
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args = listOf("src/**/*.kt")
    }

    val ktlintFormat by tasks.creating(JavaExec::class) {
        inputs.files(inputFiles)
        outputs.dir(outputDir)

        description = "Fix Kotlin code style deviations."
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args = listOf("-F", "src/**/*.kt")
    }

    packagingOptions {
        resources {
            excludes += listOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0"
            )
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }

    kapt {
        correctErrorTypes = true
    }

    if (project.hasProperty("devBuild")) {
        splits.abi.isEnable = false
        splits.density.isEnable = false
        aaptOptions.cruncherEnabled = false
    }

    tasks.getByPath("detekt").onlyIf { gradle.startParameter.taskNames.contains("detekt") }
}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))

    implementation(project(":currency:currency-di"))
    implementation(project(":currency:currency-data:currency-data-model"))
    implementation(project(":currency:currency-data:currency-data-contract"))
    implementation(project(":currency:currency-data:currency-data-impl"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-ui"))

    implementation(project(":budget:budget-di"))
    implementation(project(":budget:budget-data:budget-data-model"))
    implementation(project(":budget:budget-data:budget-data-contract"))
    implementation(project(":budget:budget-data:budget-data-impl"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))
    implementation(project(":budget:budget-domain:budget-domain-impl"))
    implementation(project(":budget:budget-ui"))

    implementation(project(":user-info:user-di"))
    implementation(project(":user-info:user-data:user-data-model"))
    implementation(project(":user-info:user-domain:user-domain-contract"))
    implementation(project(":user-info:user-ui"))

    implementation(project(":prepopulate:prepopulate-ui"))

    implementation(project(":goals:goals-di"))
    implementation(project(":goals:goals-data:goals-data-model"))
    implementation(project(":goals:goals-data:goals-data-contract"))
    implementation(project(":goals:goals-data:goals-data-impl"))
    implementation(project(":goals:goals-domain:goals-domain-contract"))
    implementation(project(":goals:goals-domain:goals-domain-impl"))

    implementation(libs.kotlinJdk)
    implementation(libs.kotlinDatetime)

    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.core)
    implementation(libs.coreRuntime)
    implementation(libs.constraintLayout)

    implementation(libs.fragment)

    implementation(libs.firebase)
    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseUi)
    implementation(libs.googlePlayServicesAuth)

    implementation(libs.firebaseAnalytics)
    implementation(libs.firebaseCrashlytics)
    implementation(libs.firebaseStorage)
    implementation(libs.firebaseConfig)

    implementation(libs.hilt)
    implementation(libs.hiltNavigationCompose)
    kapt(libs.hiltCompiler)
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.bundles.navigation)

    implementation(libs.splashScreen)

    implementation(libs.glide)
    annotationProcessor(libs.glideCompiler)
    implementation(libs.coil)

    implementation(libs.bundles.retrofit)

    implementation(libs.paging)
    implementation(libs.bundles.room)
    kapt(libs.roomCompiler)

    implementation(libs.viewpager)

    implementation(libs.coroutinesCore)

    implementation(libs.lifecycle)
    implementation(libs.lifecycleRuntime)
    implementation(libs.lifecycleExtension)
    implementation(libs.lifecycleCommon)
    implementation(libs.lifecycleViewModel)

    implementation(libs.bundles.compose)
    implementation(libs.bundles.composeMaterial3)
    implementation(libs.composeActivity)
    implementation(libs.composeViewModel)
    implementation(libs.composeRuntime)
    implementation(libs.composeConstraintLayout)
    debugImplementation(libs.composeToolingUi)

    implementation(libs.worker)
    implementation(libs.workerHilt)

    implementation(libs.klaxon)
    implementation(libs.timber)

    implementation(libs.billing)

    implementation(libs.androidChart)
    implementation(libs.dotsIndicator)
}
