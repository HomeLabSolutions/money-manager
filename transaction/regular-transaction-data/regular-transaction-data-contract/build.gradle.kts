plugins {
    id("kotlin")
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))

    implementation(libs.kotlinx.coroutines.core)
}
