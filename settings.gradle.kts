rootProject.name = "MoneyManager"
include(":app")

include(":core:common")
include(":core:common-android")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")

include(":currency:data:repo")
include(":currency:data:repo-impl")
include(":currency:domain:interactor")
include(":currency:domain:interactor-impl")
include(":currency:ui")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
