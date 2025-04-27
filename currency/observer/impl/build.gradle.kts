plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":budget:domain:contract"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:observer:contract"))
    implementation(libs.javax.inject)
}
