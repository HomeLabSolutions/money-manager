plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":goals:goals-domain:goals-domain-model"))

    implementation(libs.coroutinesCore)
}
