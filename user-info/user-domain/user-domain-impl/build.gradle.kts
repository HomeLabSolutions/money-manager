plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":user-info:user-data:user-data-model"))
    implementation(project(":user-info:user-data:user-data-contract"))
    implementation(project(":user-info:user-domain:user-domain-contract"))

    implementation(libs.coroutinesCore)
}