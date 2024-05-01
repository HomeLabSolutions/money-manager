plugins {
    id("kotlin")
}

dependencies {
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
}
