plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":currency:currency-domain:currency-domain-model"))

    implementation(libs.coroutinesCore)
}
