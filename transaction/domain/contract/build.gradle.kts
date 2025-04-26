plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.transaction.domain.contract"
}

dependencies {
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":core:common"))
    implementation(project(":transaction:domain:model"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.paging.common)
}
