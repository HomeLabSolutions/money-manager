rootProject.name = "MoneyManager"
include(":app")

include(":core:common")
include(":core:common-android")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")

include(":currency:currency-di")
include(":currency:currency-data:currency-data-contract")
include(":currency:currency-data:currency-data-impl")
include(":currency:currency-domain:currency-domain-contract")
include(":currency:currency-domain:currency-domain-impl")
include(":currency:ui")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
