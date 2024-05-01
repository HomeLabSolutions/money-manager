plugins {
    id("moneymanager.android.library")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.d9tilov.android.backup_data_impl"
}

dependencies {
    ksp(libs.hilt.android.compiler)

    implementation(platform(libs.firebase.bom))
    implementation(project(":backup:backup-data:backup-data-contract"))
    implementation(project(":backup:backup-domain:backup-domain-contract"))
    implementation(project(":backup:backup-domain:backup-domain-model"))
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(libs.androidx.work.runtime)
    implementation(libs.dagger)
    implementation(libs.firebase.config)
    implementation(libs.firebase.storage)
    implementation(libs.hilt.common)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.timber)
}
