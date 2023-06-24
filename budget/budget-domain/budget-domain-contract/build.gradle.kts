plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":budget:budget-domain:budget-domain-model"))

    implementation(libs.coroutinesCore)
}
