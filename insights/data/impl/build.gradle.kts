plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.insights.data.impl"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(project(":category:data:contract"))
    implementation(project(":category:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":insights:data:contract"))
    implementation(project(":insights:domain:contract"))
    implementation(project(":insights:domain:model"))
    implementation(project(":transaction:data:contract"))
    implementation(project(":transaction:domain:model"))
    implementation(project(":user-info:domain:model"))
    implementation(libs.firebase.functions)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk.core)
}
