plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
}

android {
    namespace = "com.d9tilov.android.profile.ui"
}

dependencies {

    implementation(project(":analytics:domain"))
    implementation(project(":backup:data:impl"))
    implementation(project(":billing:domain:contract"))
    implementation(project(":budget:domain:contract"))
    implementation(project(":budget:domain:model"))
    implementation(project(":category:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:model"))
    implementation(project(":transaction:regular:domain:contract"))
    implementation(project(":transaction:regular:domain:model"))
    implementation(project(":user-info:domain:contract"))
    implementation(project(":user-info:domain:model"))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.appcompat)
    implementation(libs.coil)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.ui.auth)
    implementation(libs.play.services.auth)
}
