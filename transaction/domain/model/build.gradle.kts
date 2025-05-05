plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":category:domain:model"))
    implementation(project(":core:common"))
}
