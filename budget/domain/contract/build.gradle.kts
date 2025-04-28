plugins {
    id("moneymanager.android.library.kotlin")
}

dependencies {

<<<<<<<< HEAD:currency/data/contract/build.gradle.kts
    implementation(project(":currency:domain:model"))
========
    implementation(project(":budget:domain:model"))
>>>>>>>> f239e1c1 (Statistics (#29)):budget/domain/contract/build.gradle.kts
    implementation(libs.kotlinx.coroutines.core)
}
