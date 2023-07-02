import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
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

    namespace = "com.d9tilov.moneymanager"
    val compileSdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra
    val targetSdkVersion: Int by rootProject.extra
    compileSdk = compileSdkVersion

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
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
        jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    }

    val ktlintFormat by tasks.creating(JavaExec::class) {
        inputs.files(inputFiles)
        outputs.dir(outputDir)

        description = "Fix Kotlin code style deviations."
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args = listOf("-F", "src/**/*.kt")
        jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
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
        kotlinCompilerExtensionVersion = "1.4.4"
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
    implementation(project(":core:designsystem"))

    implementation(project(":incomeexpense:incomeexpense-ui"))
    implementation(project(":statistics:statistics-ui"))
    implementation(project(":currency:currency-ui"))
    implementation(project(":profile:profile-ui"))
    implementation(project(":backup:backup-ui"))

    implementation(project(":billing:billing-domain:billing-domain-contract"))

    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))

    implementation(libs.appCompat)
    implementation(libs.material)

    implementation(libs.firebase)
    implementation(libs.googlePlayServicesAuth)
    implementation(libs.firebaseAnalytics)
    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseCrashlytics)
    implementation(libs.firebaseStorage)
    implementation(libs.firebaseConfig)

    implementation(libs.hilt)
    kapt(libs.hiltAndroidCompiler)

    implementation(libs.navigation)

    implementation(libs.glide)
    kapt(libs.glideCompiler)

    implementation(libs.composeMaterial3)
    implementation(libs.composeFoundation)

    implementation(libs.timber)
}
