plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit)
}
