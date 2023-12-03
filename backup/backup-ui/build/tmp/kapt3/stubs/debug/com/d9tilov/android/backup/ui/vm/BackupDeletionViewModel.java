package com.d9tilov.android.backup.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/d9tilov/android/backup/ui/vm/BackupDeletionViewModel;", "Lcom/d9tilov/android/common/android/ui/base/BaseViewModel;", "Lcom/d9tilov/android/backup/ui/navigator/SettingsBackupDeletionNavigator;", "backupInteractor", "Lcom/d9tilov/android/backup/domain/contract/BackupInteractor;", "(Lcom/d9tilov/android/backup/domain/contract/BackupInteractor;)V", "backup-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class BackupDeletionViewModel extends com.d9tilov.android.common.android.ui.base.BaseViewModel<com.d9tilov.android.backup.ui.navigator.SettingsBackupDeletionNavigator> {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.backup.domain.contract.BackupInteractor backupInteractor = null;
    
    @javax.inject.Inject
    public BackupDeletionViewModel(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.backup.domain.contract.BackupInteractor backupInteractor) {
        super();
    }
}