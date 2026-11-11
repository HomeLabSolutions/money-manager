plugins {
    id("moneymanager.android.library.kotlin")
    id("moneymanager.di")
}

dependencies {
    implementation(project(":backup:domain:contract"))
    implementation(project(":backup:domain:model"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
}
