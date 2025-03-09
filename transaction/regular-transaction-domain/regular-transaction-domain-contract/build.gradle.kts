plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":core:common"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))
    implementation(libs.kotlinx.coroutines.core)
}
