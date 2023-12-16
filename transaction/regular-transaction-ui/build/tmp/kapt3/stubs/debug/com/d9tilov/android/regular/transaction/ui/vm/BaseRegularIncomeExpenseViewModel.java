package com.d9tilov.android.regular.transaction.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\t"}, d2 = {"Lcom/d9tilov/android/regular/transaction/ui/vm/BaseRegularIncomeExpenseViewModel;", "T", "Lcom/d9tilov/android/regular/transaction/ui/navigator/BaseRegularIncomeExpenseNavigator;", "Lcom/d9tilov/android/common/android/ui/base/BaseViewModel;", "()V", "onCheckClicked", "", "regularTransaction", "Lcom/d9tilov/android/regular/transaction/domain/model/RegularTransaction;", "regular-transaction-ui_debug"})
public abstract class BaseRegularIncomeExpenseViewModel<T extends com.d9tilov.android.regular.transaction.ui.navigator.BaseRegularIncomeExpenseNavigator> extends com.d9tilov.android.common.android.ui.base.BaseViewModel<T> {
    
    public BaseRegularIncomeExpenseViewModel() {
        super();
    }
    
    public abstract void onCheckClicked(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.model.RegularTransaction regularTransaction);
}