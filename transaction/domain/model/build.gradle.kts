plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(project(":category:domain:model"))
    implementation(project(":core:common"))
}
