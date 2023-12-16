package com.d9tilov.android.settings.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010\u0019\u001a\u00020\u001aJ\u000e\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001dJ\u0006\u0010\u001e\u001a\u00020\u001aJ\u0006\u0010\u001f\u001a\u00020\u001aR\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R/\u0010\u000e\u001a\u0004\u0018\u00010\r2\b\u0010\f\u001a\u0004\u0018\u00010\r8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/d9tilov/android/settings/ui/vm/SettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "backupInteractor", "Lcom/d9tilov/android/backup/domain/contract/BackupInteractor;", "userInteractor", "Lcom/d9tilov/android/user/domain/contract/UserInteractor;", "billingInteractor", "Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;", "(Lcom/d9tilov/android/backup/domain/contract/BackupInteractor;Lcom/d9tilov/android/user/domain/contract/UserInteractor;Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/d9tilov/android/settings/ui/vm/SettingsUiState;", "<set-?>", "", "message", "getMessage", "()Ljava/lang/Integer;", "setMessage", "(Ljava/lang/Integer;)V", "message$delegate", "Landroidx/compose/runtime/MutableState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "backup", "", "changeFiscalDay", "day", "", "deleteBackup", "save", "settings-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class SettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.backup.domain.contract.BackupInteractor backupInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.user.domain.contract.UserInteractor userInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.d9tilov.android.settings.ui.vm.SettingsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.settings.ui.vm.SettingsUiState> uiState = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.compose.runtime.MutableState message$delegate = null;
    
    @javax.inject.Inject
    public SettingsViewModel(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.backup.domain.contract.BackupInteractor backupInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.user.domain.contract.UserInteractor userInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.billing.domain.contract.BillingInteractor billingInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.settings.ui.vm.SettingsUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getMessage() {
        return null;
    }
    
    public final void setMessage(@org.jetbrains.annotations.Nullable
    java.lang.Integer p0) {
    }
    
    public final void backup() {
    }
    
    public final void deleteBackup() {
    }
    
    public final void changeFiscalDay(@org.jetbrains.annotations.NotNull
    java.lang.String day) {
    }
    
    public final void save() {
    }
}