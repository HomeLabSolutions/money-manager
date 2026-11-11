package com.d9tilov.android.backup.di

import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.backup.domain.impl.BackupInteractorImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface BackupDomainModule {
    @Binds
    val BackupInteractorImpl.provideBackupInteractor: BackupInteractor
}
