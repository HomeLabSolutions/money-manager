plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {

    implementation(project(":user-info:domain:model"))
    implementation(libs.kotlinx.coroutines.core)
}
