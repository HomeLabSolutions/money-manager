plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":currency:currency-data:currency-data-contract"))

    implementation(libs.coroutinesCore)
}
