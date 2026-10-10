plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":insights:domain:model"))
    implementation(project(":user-info:domain:model"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
}
