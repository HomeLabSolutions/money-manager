package com.d9tilov.android.backup.di

import com.d9tilov.android.backup.data.contract.BackupManager
import com.d9tilov.android.backup.data.contract.BackupSource
import com.d9tilov.android.backup.data.impl.BackupDataRepo
import com.d9tilov.android.backup.data.impl.BackupLocalSource
import com.d9tilov.android.backup.data.impl.BackupManagerImpl
import com.d9tilov.android.backup.domain.contract.BackupRepo
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface BackupDataModule {
    @Binds
    val BackupDataRepo.provideBackupRepo: BackupRepo

    @Binds
    val BackupLocalSource.provideBackupLocalSource: BackupSource

    @Binds
    val BackupManagerImpl.provideBackupManager: BackupManager
}
