plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    api(libs.kotlinx.datetime)

    testImplementation(libs.junit)
}
