plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":billing:domain:contract"))
    implementation(project(":billing:domain:model"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:model"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
