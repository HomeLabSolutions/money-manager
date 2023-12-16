package com.d9tilov.android.regular.transaction.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tH\u0016R\u001d\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/d9tilov/android/regular/transaction/ui/vm/RegularExpenseViewModel;", "Lcom/d9tilov/android/regular/transaction/ui/vm/BaseRegularIncomeExpenseViewModel;", "Lcom/d9tilov/android/regular/transaction/ui/navigator/RegularExpenseNavigator;", "regularTransactionInteractor", "Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionInteractor;", "(Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionInteractor;)V", "regularExpenseTransactionList", "Lkotlinx/coroutines/flow/SharedFlow;", "", "Lcom/d9tilov/android/regular/transaction/domain/model/RegularTransaction;", "getRegularExpenseTransactionList", "()Lkotlinx/coroutines/flow/SharedFlow;", "onCheckClicked", "", "regularTransaction", "regular-transaction-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class RegularExpenseViewModel extends com.d9tilov.android.regular.transaction.ui.vm.BaseRegularIncomeExpenseViewModel<com.d9tilov.android.regular.transaction.ui.navigator.RegularExpenseNavigator> {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor regularTransactionInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.SharedFlow<java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransaction>> regularExpenseTransactionList = null;
    
    @javax.inject.Inject
    public RegularExpenseViewModel(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor regularTransactionInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.SharedFlow<java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransaction>> getRegularExpenseTransactionList() {
        return null;
    }
    
    @java.lang.Override
    public void onCheckClicked(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.model.RegularTransaction regularTransaction) {
    }
}