plugins {
    id("moneymanager.android.library.kotlin")
    id("moneymanager.di")
}

dependencies {
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit)
}
