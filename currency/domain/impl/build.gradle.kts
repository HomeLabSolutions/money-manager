plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {
    implementation(project(":backup:domain:contract"))
    implementation(project(":backup:domain:model"))
    implementation(project(":core:common"))
<<<<<<<< HEAD:backup/domain/impl/build.gradle.kts
========
    implementation(project(":currency:domain:contract"))
    implementation(project(":currency:domain:model"))
>>>>>>>> f239e1c1 (Statistics (#29)):currency/domain/impl/build.gradle.kts
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
