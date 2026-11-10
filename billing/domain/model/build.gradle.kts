plugins {
    id("moneymanager.android.library.kotlin")
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(project(":currency:domain:model"))
    implementation(libs.kotlinx.serialization.core)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.serialization.json)
}
