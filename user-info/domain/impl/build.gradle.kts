plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":user-info:domain:contract"))
    implementation(project(":user-info:domain:model"))
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk.core)
}
