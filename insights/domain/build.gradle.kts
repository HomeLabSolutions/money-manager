plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":user-info:domain:model"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.core)
}
