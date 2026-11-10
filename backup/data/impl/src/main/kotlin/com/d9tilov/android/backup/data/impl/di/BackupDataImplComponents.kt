package com.d9tilov.android.backup.data.impl.di

import com.d9tilov.android.backup.data.impl.PeriodicBackupWorker
import org.koin.dsl.module
import org.koin.plugin.module.dsl.worker

val backupDataImplComponents =
    module {
        worker<PeriodicBackupWorker>()
    }
