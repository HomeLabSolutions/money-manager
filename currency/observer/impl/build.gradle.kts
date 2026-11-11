plugins {
    id("moneymanager.android.library.kotlin")
    id("moneymanager.di")
}

dependencies {
    implementation(project(":budget:domain:contract"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:observer:contract"))
}
