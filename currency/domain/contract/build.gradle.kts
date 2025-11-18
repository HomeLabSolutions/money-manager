plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":currency:domain:model"))

    implementation(libs.kotlinx.coroutines.core)
}
