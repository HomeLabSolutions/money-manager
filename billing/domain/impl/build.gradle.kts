plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.billing.domain.impl"
}

dependencies {

    implementation(project(":billing:domain:contract"))
    implementation(project(":billing:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:model"))
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.config.ktx)
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.ui.auth)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.play.services.auth)
    implementation(libs.timber)
}
