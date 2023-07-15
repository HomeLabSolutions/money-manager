plugins {
    id("moneymanager.android.library")
}

android {
    namespace = "com.d9tilov.android.goals_data_impl"

    
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))

    implementation(project(":goals:goals-domain:goals-domain-model"))
    implementation(project(":goals:goals-domain:goals-domain-contract"))
    implementation(project(":goals:goals-data:goals-data-contract"))

    implementation(libs.coroutinesCore)
}
