plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":category:category-domain:category-domain-model"))

    implementation(libs.coroutinesCore)
}
