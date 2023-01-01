plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":currency:currency-data:currency-data-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":goals:goals-data:goals-data-contract"))
    implementation(project(":goals:goals-data:goals-data-model"))
    implementation(project(":goals:goals-domain:goals-domain-contract"))
    implementation(project(":goals:goals-domain:goals-domain-model"))

    implementation(libs.coroutinesCore)
}
