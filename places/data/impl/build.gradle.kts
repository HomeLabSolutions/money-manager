plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.places.data.impl"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":places:data:contract"))
    implementation(project(":places:domain:contract"))
    implementation(project(":places:domain:model"))
    implementation(libs.javax.inject)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
