plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":category:category-data:category-data-model"))
    implementation(project(":category:category-data:category-data-contract"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":category:category-domain:category-domain-contract"))

    implementation(libs.coroutinesCore)
}
