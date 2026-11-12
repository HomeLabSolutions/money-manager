plugins {
    id("moneymanager.android.library.kotlin")
    id("moneymanager.di")
}

dependencies {
    implementation(project(":billing:domain:contract"))
    implementation(project(":billing:domain:model"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:model"))
    implementation(libs.kotlinx.coroutines.core)
}
