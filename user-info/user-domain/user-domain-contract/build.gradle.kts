plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":user-info:user-domain:user-domain-model"))

    implementation(libs.coroutinesCore)
}
