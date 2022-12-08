rootProject.name = "MoneyManager"
include(":app")
include(":core:common")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
include(":core:mylibrary")
