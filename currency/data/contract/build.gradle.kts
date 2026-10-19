plugins {
    id("moneymanager.android.library.kotlin")
    alias(libs.plugins.serialization)
}

dependencies {

    implementation(project(":currency:domain:model"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.core)
}
