import java.io.FileInputStream
import java.util.Properties

plugins {
    id("moneymanager.android.application")
    id("moneymanager.android.hilt")
    id("moneymanager.android.application.compose")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("io.gitlab.arturbosch.detekt")
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
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
    }
    dataBinding {
        isEnabled = true
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

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))

    implementation(project(":transaction:transaction-domain:transaction-domain-contract"))

    implementation(project(":currency:currency-data:currency-data-impl"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-observer:currency-observer-contract"))

    implementation(project(":backup:backup-domain:backup-domain-contract"))
    implementation(project(":backup:backup-di"))

    implementation(project(":user-info:user-domain:user-domain-model"))
    implementation(project(":user-info:user-domain:user-domain-contract"))
    implementation(project(":user-info:user-data:user-data-impl"))
    implementation(project(":user-info:user-di"))

    implementation(project(":budget:budget-domain:budget-domain-model"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))

    implementation(project(":billing:billing-domain:billing-domain-contract"))

    implementation(project(":category:category-domain:category-domain-contract"))

    implementation(project(":budget:budget-ui"))
    implementation(project(":currency:currency-ui"))
    implementation(project(":incomeexpense:incomeexpense-ui"))
    implementation(project(":statistics:statistics-ui"))
    implementation(project(":profile:profile-ui"))

    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.navigation)
    implementation(libs.activity)

    implementation(libs.firebase)
    implementation(libs.firebaseUi)
    implementation(libs.googlePlayServicesAuth)
    implementation(libs.firebaseAnalytics)
    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseCrashlytics)
    implementation(libs.firebaseStorage)
    implementation(libs.firebaseConfig)

    implementation(libs.composeUi)
    implementation(libs.composeViewModel)
    implementation(libs.composeMaterial3)
    implementation(libs.composeMaterial3WindowSize)
    implementation(libs.composeFoundation)
    implementation(libs.composeToolingPreview)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.composeMaterialIconsCore)
    implementation(libs.composeMaterialIconsExtended)
    implementation(libs.composeRuntime)

    implementation(libs.androidx.tracing.ktx)

    implementation(libs.timber)
    implementation(libs.serializationKotlin)

    implementation(libs.splashScreen)
}
