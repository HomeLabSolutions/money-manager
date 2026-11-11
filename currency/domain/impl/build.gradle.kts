plugins {
    id("moneymanager.android.library.kotlin")
    id("moneymanager.di")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:model"))
    implementation(project(":places:domain:model"))
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk.core)
}
