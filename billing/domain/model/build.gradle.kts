import com.android.moneymanager.gradle.extensions.ksp

plugins {
    id("com.google.devtools.ksp")
    id("moneymanager.android.library.kotlin")
}
dependencies {
    implementation(project(":currency:domain:model"))

    implementation(libs.moshi)

    ksp(libs.moshi.kotlin.codegen)
}
