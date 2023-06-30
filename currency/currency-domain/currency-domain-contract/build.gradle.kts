plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":currency:currency-domain:currency-domain-model"))

    implementation(libs.coroutinesCore)
}
