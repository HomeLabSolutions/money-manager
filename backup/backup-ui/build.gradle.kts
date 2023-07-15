plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    
}

android {
    namespace = "com.d9tilov.android.backup_ui"


    
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))

    implementation(project(":backup:backup-domain:backup-domain-contract"))
    implementation(project(":backup:backup-di"))

    implementation(libs.lifecycleViewModel)

    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseStorage)
    implementation(libs.firebaseConfig)

    implementation(libs.timber)
}

