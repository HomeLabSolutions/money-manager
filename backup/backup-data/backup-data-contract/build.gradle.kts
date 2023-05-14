plugins {
    id("kotlin")
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":backup:backup-data:backup-data-model"))

    implementation(libs.coroutinesCore)
}
