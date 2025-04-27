plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":category:category-domain:contract"))
    implementation(project(":category:category-domain:model"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
}
