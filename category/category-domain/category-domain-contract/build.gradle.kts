plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":category:category-data:category-data-contract"))
    implementation(project(":category:category-data:category-data-model"))

    implementation(libs.coroutinesCore)
}
