plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":currency:currency-data:currency-data-model"))
    implementation(project(":currency:currency-data:currency-data-contract"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))

    implementation(libs.coroutinesCore)
}
