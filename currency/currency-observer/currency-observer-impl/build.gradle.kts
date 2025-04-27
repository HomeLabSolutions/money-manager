plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":budget:domain:contract"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-observer:currency-observer-contract"))
    implementation(libs.javax.inject)
}
