plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.places.data.contract"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
}
