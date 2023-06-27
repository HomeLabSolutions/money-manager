plugins {
    id("kotlin")
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":transaction:regular-transaction-data:regular-transaction-data-model"))

    implementation(libs.coroutinesCore)
}
