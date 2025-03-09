plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.currency_data_impl"

    lint {
        disable.apply {
            add("CoroutineCreationDuringComposition")
            add("FlowOperatorInvokedInComposition")
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":currency:currency-data:currency-data-contract"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime)
    implementation(libs.dagger)
    implementation(libs.hilt.common)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.startup)
    implementation(libs.timber)
}
