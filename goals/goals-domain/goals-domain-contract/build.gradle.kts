plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":goals:goals-domain:goals-domain-model"))

    implementation(libs.coroutinesCore)
}
