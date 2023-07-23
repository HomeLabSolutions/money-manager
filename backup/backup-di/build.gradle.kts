plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.backup_di"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":backup:backup-data:backup-data-contract"))
    implementation(project(":backup:backup-data:backup-data-impl"))
    implementation(project(":backup:backup-domain:backup-domain-contract"))
    implementation(project(":backup:backup-domain:backup-domain-impl"))
}
