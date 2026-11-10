package com.d9tilov.android.backup.di

import com.d9tilov.android.backup.data.contract.BackupSource
import com.d9tilov.android.backup.data.impl.BackupDataRepo
import com.d9tilov.android.backup.data.impl.BackupLocalSource
import com.d9tilov.android.backup.data.impl.backupManagerImplModule
import com.d9tilov.android.backup.domain.contract.BackupRepo
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val backupDataModule =
    module {
        factory<BackupDataRepo>() bind BackupRepo::class
        factory<BackupLocalSource>() bind BackupSource::class
        includes(backupManagerImplModule)
    }
