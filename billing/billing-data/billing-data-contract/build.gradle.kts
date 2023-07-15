plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.billing_data_contract"

    
}

dependencies {

    implementation(libs.coroutinesCore)
    implementation(libs.billing)
}
