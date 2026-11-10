package com.d9tilov.android.database.di

import com.d9tilov.android.database.DatabaseBackupStore
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val coreDatabaseComponents =
    module {
        single<DatabaseBackupStore>()
    }
