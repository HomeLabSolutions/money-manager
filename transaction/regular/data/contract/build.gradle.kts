plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":category:domain:model"))
    implementation(project(":shared"))
    implementation(project(":transaction:regular:domain:model"))
    implementation(libs.kotlinx.coroutines.core)
}
