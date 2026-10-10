plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.insights.domain.impl"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":insights:domain:contract"))
    implementation(project(":insights:domain:model"))
    implementation(project(":transaction:domain:contract"))
    implementation(project(":transaction:domain:model"))
    implementation(project(":user-info:domain:model"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk.core)
}
