plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":category:domain:contract"))
    implementation(project(":category:domain:model"))
    implementation(project(":core:common"))
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:model"))
    implementation(project(":transaction:regular:domain:contract"))
    implementation(project(":transaction:regular:domain:model"))
    implementation(libs.kotlinx.coroutines.core)
}
