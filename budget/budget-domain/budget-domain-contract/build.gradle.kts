plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":budget:budget-data:budget-data-model"))
    implementation(project(":budget:budget-data:budget-data-contract"))

    implementation(libs.coroutinesCore)
}
