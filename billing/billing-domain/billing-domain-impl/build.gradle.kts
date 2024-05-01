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

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.timber)
    implementation(libs.retrofit.moshi)

    implementation(libs.firebase.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.ui.auth)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.config)
}
