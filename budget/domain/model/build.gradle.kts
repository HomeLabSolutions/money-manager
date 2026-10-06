plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(project(":core:common"))
}
