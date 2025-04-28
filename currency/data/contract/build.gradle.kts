plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {

<<<<<<<< HEAD:budget/domain/contract/build.gradle.kts
    implementation(project(":budget:domain:model"))
========
    implementation(project(":currency:domain:model"))
>>>>>>>> f239e1c1 (Statistics (#29)):currency/data/contract/build.gradle.kts
    implementation(libs.kotlinx.coroutines.core)
}
