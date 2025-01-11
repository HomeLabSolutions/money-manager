plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":user-info:user-domain:user-domain-model"))

    implementation(libs.kotlinx.coroutines.core)
}
