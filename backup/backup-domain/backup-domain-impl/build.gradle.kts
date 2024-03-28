plugins {
    id("kotlin")
}

dependencies {

    implementation(project(":core:common"))

    implementation(project(":backup:backup-domain:backup-domain-model"))
    implementation(project(":backup:backup-domain:backup-domain-contract"))

    implementation(libs.coroutinesCore)
}
