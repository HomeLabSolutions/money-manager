plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.backup.data.impl"

    lint {
        disable.add("FlowOperatorInvokedInComposition")
    }
}

dependencies {
    implementation(project(":analytics:domain"))
    implementation(project(":backup:data:contract"))
    implementation(project(":backup:domain:contract"))
    implementation(project(":backup:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))

    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime)
    implementation(libs.dagger)
    implementation(libs.firebase.config.ktx)
    implementation(libs.firebase.storage)
    implementation(libs.hilt.common)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.timber)
}
