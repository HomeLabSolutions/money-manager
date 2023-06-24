plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":budget:budget-data:budget-data-model"))
    implementation(project(":budget:budget-data:budget-data-contract"))
    implementation(project(":budget:budget-domain:budget-domain-contract"))
    
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))

    implementation(libs.coroutinesCore)
}
