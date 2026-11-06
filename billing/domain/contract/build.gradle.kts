plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":billing:domain:model"))
    implementation(libs.kotlinx.coroutines.core)
}
