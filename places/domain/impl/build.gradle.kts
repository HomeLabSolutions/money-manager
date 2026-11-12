plugins {
    id("moneymanager.android.library.kotlin")
    id("moneymanager.di")
}

dependencies {
    implementation(project(":places:domain:contract"))
    implementation(project(":places:domain:model"))
}
