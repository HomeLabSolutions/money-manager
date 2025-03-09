plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":budget:budget-domain:budget-domain-contract"))
    implementation(project(":budget:budget-domain:budget-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
}
