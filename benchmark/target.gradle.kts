import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.artifacts.VersionCatalogsExtension

extensions.configure<ApplicationExtension> {
    buildTypes.create("benchmark") {
        initWith(buildTypes.getByName("release"))
        applicationIdSuffix = ".benchmark"
        isDebuggable = false
        signingConfig = signingConfigs.getByName("debug")
        matchingFallbacks += "release"
        manifestPlaceholders["appName"] = "Money Manager benchmark"
    }
    sourceSets.getByName("benchmark") {
        manifest.srcFile(rootProject.file("benchmark/target/AndroidManifest.xml"))
    }
}

val catalog = extensions.getByType<VersionCatalogsExtension>().named("startupBenchmark")
dependencies.add("benchmarkImplementation", catalog.findLibrary("profileinstaller").get())
