plugins {
    id("kotlin")
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":user-info:user-data:user-data-model"))

    implementation(libs.coroutinesCore)
}
