plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
}

android {
    namespace = "com.d9tilov.android.insights.data"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))
    implementation(project(":category:data:contract"))
    implementation(project(":category:domain:model"))
    implementation(project(":insights:domain"))
    implementation(project(":transaction:data:contract"))
    implementation(project(":transaction:domain:model"))
    implementation(project(":user-info:domain:model"))
    implementation(libs.firebase.functions)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit)
}
