plugins {
    id("com.android.test")
}

android {
    namespace = "com.d9tilov.moneymanager.startup"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
        targetSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        create("benchmark") {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += "release"
        }
    }
}

androidComponents {
    beforeVariants(selector().all()) {
        it.enable = it.buildType == "benchmark"
    }
}

dependencies {
    implementation(startupBenchmark.benchmark.junit4)
    implementation(startupBenchmark.benchmark.macro)
    implementation(startupBenchmark.junit)
    implementation(startupBenchmark.test.junit)
    implementation(startupBenchmark.test.runner)
}
