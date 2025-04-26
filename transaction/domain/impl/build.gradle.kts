plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.transaction.domain.impl"
}

dependencies {
    implementation(project(":budget:budget-domain:budget-domain-contract"))
    implementation(project(":budget:budget-domain:budget-domain-model"))
    implementation(project(":category:category-domain:category-domain-contract"))
    implementation(project(":category:category-domain:category-domain-model"))
    implementation(project(":core:common"))
    implementation(project(":currency:currency-domain:currency-domain-contract"))
    implementation(project(":currency:currency-domain:currency-domain-model"))
    implementation(project(":transaction:domain:contract"))
    implementation(project(":transaction:domain:model"))
    implementation(project(":transaction:regular:domain:contract"))
    implementation(project(":transaction:regular:domain:model"))
    implementation(project(":user-info:domain:contract"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.paging.common)
}
