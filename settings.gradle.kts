rootProject.name = "MoneyManager"
include(":app")

include(":core:common")
include(":core:common-android")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")

include(":currency:currency-di")
include(":currency:currency-data:currency-data-model")
include(":currency:currency-data:currency-data-contract")
include(":currency:currency-data:currency-data-impl")
include(":currency:currency-domain:currency-domain-model")
include(":currency:currency-domain:currency-domain-contract")
include(":currency:currency-domain:currency-domain-impl")
include(":currency:ui")

include(":budget:budget-di")
include(":budget:budget-data:budget-data-model")
include(":budget:budget-data:budget-data-contract")
include(":budget:budget-data:budget-data-impl")
include(":budget:budget-domain:budget-domain-contract")
include(":budget:budget-domain:budget-domain-impl")
include(":budget:budget-ui")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
