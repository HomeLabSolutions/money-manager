package com.d9tilov.android.backup.di

import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.backup.domain.impl.BackupInteractorImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val backupDomainModule =
    module {
        factory<BackupInteractorImpl>() bind BackupInteractor::class
    }
