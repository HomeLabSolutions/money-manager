plugins {
    id("moneymanager.android.library.kotlin")
    id("moneymanager.di")
}

dependencies {
    implementation(libs.bignum)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit)
}
