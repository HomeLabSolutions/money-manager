plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.user_data_impl"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":user-info:user-data:user-data-contract"))
    implementation(project(":user-info:user-domain:user-domain-contract"))
    implementation(project(":user-info:user-domain:user-domain-model"))
    implementation(libs.firebase.ui.auth)
    implementation(libs.kotlinx.coroutines.core)
}
