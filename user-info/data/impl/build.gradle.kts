plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.user.data.impl"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":user-info:data:contract"))
    implementation(project(":user-info:domain:contract"))
    implementation(project(":user-info:domain:model"))
    implementation(libs.firebase.ui.auth)
    implementation(libs.kotlinx.coroutines.core)
}
