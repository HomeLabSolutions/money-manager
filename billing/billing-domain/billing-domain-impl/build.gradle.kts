plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.billing_domain_impl"

    
}

dependencies {

    implementation(project(":core:common"))

    implementation(project(":billing:billing-domain:billing-domain-model"))
    implementation(project(":billing:billing-domain:billing-domain-contract"))
    
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))

    implementation(libs.coroutinesCore)
    implementation(libs.timber)
    implementation(libs.retrofitMoshi)

    implementation(libs.firebase)
    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseUi)
    implementation(libs.googlePlayServicesAuth)
    implementation(libs.firebaseAnalytics)
    implementation(libs.firebaseCrashlytics)
    implementation(libs.firebaseStorage)
    implementation(libs.firebaseConfig)
}
