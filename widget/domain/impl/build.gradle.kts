plugins {
    id("moneymanager.android.library.kotlin")
    id("moneymanager.di")
}

dependencies {
    implementation(project(":billing:domain:contract"))
    implementation(project(":core:common"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:model"))
    implementation(project(":transaction:domain:contract"))
    implementation(project(":transaction:domain:model"))
    implementation(project(":user-info:domain:contract"))
    implementation(project(":user-info:domain:model"))
    implementation(project(":widget:domain:contract"))
    implementation(project(":widget:domain:model"))
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk.core)
}
