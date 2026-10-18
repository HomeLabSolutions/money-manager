plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":places:domain:model"))
}
