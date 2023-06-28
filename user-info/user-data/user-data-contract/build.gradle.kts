plugins {
    id("kotlin")
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":user-info:user-domain:user-domain-model"))

    implementation(libs.coroutinesCore)
}
