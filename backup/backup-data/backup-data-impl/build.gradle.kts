plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.backup_data_impl"
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))

    implementation(project(":backup:backup-data:backup-data-contract"))
    implementation(project(":backup:backup-domain:backup-domain-contract"))
    implementation(project(":backup:backup-domain:backup-domain-model"))

    implementation(libs.coroutinesCore)
    implementation(libs.timber)

    implementation(libs.worker)
    implementation(libs.workerHilt)

    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseStorage)
    implementation(libs.firebaseConfig)
}
