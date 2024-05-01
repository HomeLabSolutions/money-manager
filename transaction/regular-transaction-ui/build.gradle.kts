plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("moneymanager.android.library.compose")
    id("moneymanager.android.library.viewbinding")
    
    id("kotlin-parcelize")
}

android {
    namespace = "com.d9tilov.android.regular_transaction_ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:designsystem"))

    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-model"))
    implementation(project(":transaction:regular-transaction-domain:regular-transaction-domain-contract"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))

    
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    implementation(libs.material)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.compose.ui)
    
}
