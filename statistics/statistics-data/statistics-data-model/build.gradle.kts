plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.coroutinesCore)
}
