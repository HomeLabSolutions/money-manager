plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":widget:domain:model"))
    implementation(libs.kotlinx.coroutines.core)
}
