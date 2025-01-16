plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":backup:backup-domain:backup-domain-contract"))
    implementation(project(":backup:backup-domain:backup-domain-model"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
}
