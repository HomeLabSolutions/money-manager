plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {

    implementation(project(":currency:domain:model"))
    implementation(libs.kotlinx.coroutines.core)
}
