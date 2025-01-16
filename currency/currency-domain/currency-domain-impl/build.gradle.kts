plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(libs.kotlinx.coroutines.core)
}
