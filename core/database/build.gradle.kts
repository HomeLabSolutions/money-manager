plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    id("kotlin-parcelize")
    
}

android {
    namespace = "com.d9tilov.android.database"

    
}

dependencies {
    implementation(project(":core:common"))

    api(libs.room)
    implementation(libs.roomPaging)
    kapt(libs.roomCompiler)

    implementation(libs.coroutinesCore)

    
    
}
