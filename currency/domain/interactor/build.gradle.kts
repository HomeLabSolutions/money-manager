plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":currency:data:repo"))

    implementation(libs.coroutinesCore)
}
