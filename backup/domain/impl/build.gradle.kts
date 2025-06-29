plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":backup:domain:contract"))
    implementation(project(":backup:domain:model"))
    implementation(project(":core:common"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
