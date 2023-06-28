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
include(":currency:currency-domain:currency-domain-model")
include(":currency:currency-domain:currency-domain-contract")
include(":currency:currency-domain:currency-domain-impl")
include(":currency:currency-observer:currency-observer-contract")
include(":currency:currency-observer:currency-observer-impl")
include(":currency:currency-ui")

include(":budget:budget-di")
include(":budget:budget-data:budget-data-contract")
include(":budget:budget-data:budget-data-impl")
include(":budget:budget-domain:budget-domain-model")
include(":budget:budget-domain:budget-domain-contract")
include(":budget:budget-domain:budget-domain-impl")
include(":budget:budget-ui")

include(":prepopulate:prepopulate-ui")

include(":user-info:user-di")
include(":user-info:user-data:user-data-model")
include(":user-info:user-data:user-data-contract")
include(":user-info:user-data:user-data-impl")
include(":user-info:user-domain:user-domain-model")
include(":user-info:user-domain:user-domain-contract")
include(":user-info:user-domain:user-domain-impl")

include(":goals:goals-di")
include(":goals:goals-data:goals-data-contract")
include(":goals:goals-data:goals-data-impl")
include(":goals:goals-domain:goals-domain-model")
include(":goals:goals-domain:goals-domain-contract")
include(":goals:goals-domain:goals-domain-impl")
include(":goals:goals-ui")

include(":transaction:transaction-di")
include(":transaction:transaction-data:transaction-data-contract")
include(":transaction:transaction-data:transaction-data-impl")
include(":transaction:transaction-data:transaction-data-model")
include(":transaction:transaction-domain:transaction-domain-contract")
include(":transaction:transaction-domain:transaction-domain-impl")
include(":transaction:transaction-domain:transaction-domain-model")
include(":transaction:transaction-ui")
include(":transaction:regular-transaction-data:regular-transaction-data-contract")
include(":transaction:regular-transaction-data:regular-transaction-data-model")
include(":transaction:regular-transaction-data:regular-transaction-data-impl")
include(":transaction:regular-transaction-domain:regular-transaction-domain-model")
include(":transaction:regular-transaction-domain:regular-transaction-domain-contract")
include(":transaction:regular-transaction-domain:regular-transaction-domain-impl")
include(":transaction:regular-transaction-di")
include(":transaction:regular-transaction-ui")

include(":category:category-di")
include(":category:category-data")
include(":category:category-domain")
include(":category:category-data:category-data-impl")
include(":category:category-data:category-data-contract")
include(":category:category-domain:category-domain-contract")
include(":category:category-domain:category-domain-model")
include(":category:category-domain:category-domain-impl")
include(":category:category-ui")

include(":billing:billing-data:billing-data-contract")
include(":billing:billing-data:billing-data-impl")
include(":billing:billing-domain:billing-domain-contract")
include(":billing:billing-domain:billing-domain-model")
include(":billing:billing-domain:billing-domain-impl")
include(":billing:billing-di")

include(":incomeexpense")

include(":settings:settings-ui")

include(":backup:backup-data:backup-data-contract")
include(":backup:backup-data:backup-data-impl")
include(":backup:backup-domain:backup-domain-contract")
include(":backup:backup-domain:backup-domain-model")
include(":backup:backup-domain:backup-domain-impl")
include(":backup:backup-di")
include(":backup:backup-ui")

include(":statistics:statistics-domain:statistics-domain-model")
include(":statistics:statistics-ui")

include(":profile:profile-ui")

include(":splash:splash-ui")

include(":analytics:analytics-di")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
