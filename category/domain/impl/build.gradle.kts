plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":category:domain:contract"))
    implementation(project(":category:domain:model"))
    implementation(project(":core:common"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk.core)
}
