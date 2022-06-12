import java.io.FileInputStream
import java.util.Properties

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

    compileSdk = ConfigData.compileSdkVersion
    buildToolsVersion = ConfigData.buildToolsVersion

    defaultConfig {
        applicationId = "com.d9tilov.moneymanager"
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName

        buildConfigField("String", "API_KEY", keystoreProperties["currency_api_key"] as String)
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
        multiDexEnabled = true
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
        }
        debug {
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
    }
    dataBinding {
        isEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    val ktlint by configurations.creating

    dependencies {
        ktlint(Deps.ktlint)
    }

    tasks.register<JavaExec>("ktlint") {
        group = "verification"
        description = "Check Kotlin code style."
        classpath = ktlint
        mainClass.set("com.github.shyiko.ktlint.Main")
        args("--android", "src/**/*.kt")
    }

    tasks.named("check") {
        dependsOn(ktlint)
    }

    tasks.register<JavaExec>("ktlintFormat") {
        group = "formatting"
        description = "Fix Kotlin code style deviations."
        classpath = ktlint
        mainClass.set("com.github.shyiko.ktlint.Main")
        args("--android", "-F", "src/**/*.kt")
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

    kapt {
        correctErrorTypes = true
    }

    if (project.hasProperty("devBuild")) {
        splits.abi.isEnable = false
        splits.density.isEnable = false
        aaptOptions.cruncherEnabled = false
    }

}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {

    implementation(Deps.kotlinJdk)
    implementation(Deps.kotlinDatetime)

    implementation(Deps.appCompat)
    implementation(Deps.material)
    implementation(Deps.core)
    implementation(Deps.coreRuntime)
    implementation(Deps.constraintLayout)

    implementation(Deps.fragment)

    implementation(Deps.retrofit)
    implementation(Deps.retrofitMoshi)

    implementation(Deps.firebase)
    implementation(platform(Deps.firebaseBom))
    implementation(Deps.firebaseUi)
    implementation(Deps.googlePlayServicesAuth)

    implementation(Deps.firebaseAnalytics)
    implementation(Deps.firebaseAnalytics)
    implementation(Deps.firebaseCrashlytics)
    implementation(Deps.firebaseStorage)

    implementation(Deps.hilt)
    kapt(Deps.hiltCompiler)
    kapt(Deps.hiltAndroidCompiler)

    implementation(Deps.navigation)
    implementation(Deps.navigationUi)

    implementation(Deps.datastore)

    implementation(Deps.multidex)

    implementation(Deps.facebookLogin)
    implementation(Deps.twitter) {
        isTransitive = true
    }

    implementation(Deps.okHttp)
    implementation(Deps.okHttpInterceptor)

    implementation(Deps.glide)
    implementation(Deps.glideCompiler)

    implementation(Deps.stetho)
    implementation(Deps.stethoOkHttp)

    implementation(Deps.paging)
    implementation(Deps.roomRuntime)
    implementation(Deps.room)
    implementation(Deps.roomPaging)
    kapt(Deps.roomCompiler)

    implementation(Deps.sql)
    implementation(Deps.sqlCipher)

    implementation(Deps.viewpager)

    implementation(Deps.coroutinesCore)

    implementation(Deps.lifecycle)
    implementation(Deps.lifecycleRuntime)
    implementation(Deps.lifecycleExtension)
    implementation(Deps.lifecycleCommon)
    implementation(Deps.lifecycleViewModel)

    implementation(Deps.worker)
    implementation(Deps.workerHilt)

    implementation(Deps.klaxon)
    implementation(Deps.timber)

    implementation(Deps.billing)

    implementation(Deps.androidChart)
    implementation(Deps.dotsIndicator)
}
