plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":budget:domain:contract"))
    implementation(project(":budget:domain:model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
}
