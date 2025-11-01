plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":budget:domain:contract"))
    implementation(project(":budget:domain:model"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:model"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk.core)
}
