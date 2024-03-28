plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":user-info:user-domain:user-domain-model"))
    implementation(project(":user-info:user-domain:user-domain-contract"))

    implementation(libs.coroutinesCore)
}
