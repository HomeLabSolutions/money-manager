plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))

    implementation(project(":backup:backup-domain:backup-domain-model"))

    implementation(libs.coroutinesCore)
}
