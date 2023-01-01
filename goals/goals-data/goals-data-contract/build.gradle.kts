plugins {
    id("kotlin")
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":goals:goals-data:goals-data-model"))

    implementation(libs.coroutinesCore)
}
