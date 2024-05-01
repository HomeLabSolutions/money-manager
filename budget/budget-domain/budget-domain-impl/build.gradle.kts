plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":budget:budget-domain:budget-domain-model"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))

    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
}
