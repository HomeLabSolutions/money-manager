package com.d9tilov.android.backup.data.impl

import com.d9tilov.android.backup.data.contract.BackupManager
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

// Keep constructor wiring beside the class so parameter qualifiers are available to the compiler plugin.
val backupManagerImplModule =
    module {
        single<BackupManagerImpl>() bind BackupManager::class
    }
