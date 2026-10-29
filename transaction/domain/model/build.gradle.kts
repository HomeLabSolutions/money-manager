plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":category:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":places:domain:model"))
    implementation(project(":shared"))
    implementation(libs.kotlinx.datetime)
}
