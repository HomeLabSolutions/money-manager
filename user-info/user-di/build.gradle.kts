plugins {
    id("moneymanager.android.library")
    id("moneymanager.android.hilt")
    
}

android {
    namespace = "com.d9tilov.android.user_di"

    
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":user-info:user-data:user-data-contract"))
    implementation(project(":user-info:user-data:user-data-impl"))
    implementation(project(":user-info:user-domain:user-domain-contract"))
    implementation(project(":user-info:user-domain:user-domain-impl"))

    
    
}
