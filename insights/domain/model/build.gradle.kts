plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.kotlinx.datetime)
}
