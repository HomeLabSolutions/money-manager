plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":budget:budget-domain:budget-domain-model"))

    implementation(libs.kotlinx.coroutines.core)
}
