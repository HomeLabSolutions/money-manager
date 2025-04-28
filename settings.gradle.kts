rootProject.name = "MoneyManager"

include(":analytics:di")

include(":app")

include(":backup:data:contract")
include(":backup:data:impl")
include(":backup:di")
include(":backup:domain:contract")
include(":backup:domain:impl")
include(":backup:domain:model")

include(":billing:data:contract")
include(":billing:data:impl")
include(":billing:di")
include(":billing:domain:contract")
include(":billing:domain:impl")
include(":billing:domain:model")

include(":budget:data:contract")
include(":budget:data:impl")
include(":budget:di")
include(":budget:domain:contract")
include(":budget:domain:impl")
include(":budget:domain:model")
include(":budget:ui")

include(":category:data:contract")
include(":category:data:impl")
include(":category:di")
include(":category:domain:contract")
include(":category:domain:impl")
include(":category:domain:model")
include(":category:ui")

include(":core:common-android")
include(":core:common")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:network")

include(":currency:data:contract")
include(":currency:data:impl")
include(":currency:di")
include(":currency:domain:contract")
include(":currency:domain:impl")
include(":currency:domain:model")
include(":currency:observer:contract")
include(":currency:observer:impl")
include(":currency:ui")

include(":incomeexpense:ui")

include(":profile:ui")

include(":settings:ui")

include(":statistics:domain:model")
include(":statistics:ui")

include(":transaction:data:contract")
include(":transaction:data:impl")
include(":transaction:di")
include(":transaction:domain:contract")
include(":transaction:domain:impl")
include(":transaction:domain:model")
include(":transaction:regular:data:contract")
include(":transaction:regular:data:impl")
include(":transaction:regular:data:impl")
include(":transaction:regular:di")
include(":transaction:regular:domain:contract")
include(":transaction:regular:domain:impl")
include(":transaction:regular:domain:model")
include(":transaction:regular:ui")
include(":transaction:regular")
include(":transaction:ui")

include(":user-info:data:contract")
include(":user-info:data:impl")
include(":user-info:di")
include(":user-info:domain:contract")
include(":user-info:domain:impl")
include(":user-info:domain:model")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
