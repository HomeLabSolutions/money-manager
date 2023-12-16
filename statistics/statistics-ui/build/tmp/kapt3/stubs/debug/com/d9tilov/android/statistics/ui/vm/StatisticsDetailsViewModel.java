package com.d9tilov.android.statistics.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tR\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\f0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0013\u00a8\u0006\u001e"}, d2 = {"Lcom/d9tilov/android/statistics/ui/vm/StatisticsDetailsViewModel;", "Lcom/d9tilov/android/common/android/ui/base/BaseViewModel;", "Lcom/d9tilov/android/statistics/ui/navigation/StatisticsDetailsNavigator;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "transactionInteractor", "Lcom/d9tilov/android/transaction/domain/contract/TransactionInteractor;", "categoryInteractor", "Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;", "(Landroidx/lifecycle/SavedStateHandle;Lcom/d9tilov/android/transaction/domain/contract/TransactionInteractor;Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;)V", "_category", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/d9tilov/android/category/domain/model/Category;", "_transactions", "", "Lcom/d9tilov/android/transaction/domain/model/Transaction;", "category", "Lkotlinx/coroutines/flow/StateFlow;", "getCategory", "()Lkotlinx/coroutines/flow/StateFlow;", "categoryId", "", "endPeriod", "inStatistics", "", "startPeriod", "transactionType", "Lcom/d9tilov/android/core/model/TransactionType;", "transactions", "getTransactions", "statistics-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class StatisticsDetailsViewModel extends com.d9tilov.android.common.android.ui.base.BaseViewModel<com.d9tilov.android.statistics.ui.navigation.StatisticsDetailsNavigator> {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.transaction.domain.contract.TransactionInteractor transactionInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor = null;
    private final long categoryId = 0L;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.core.model.TransactionType transactionType = null;
    private final long startPeriod = 0L;
    private final long endPeriod = 0L;
    private final boolean inStatistics = false;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.d9tilov.android.category.domain.model.Category> _category = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.d9tilov.android.transaction.domain.model.Transaction>> _transactions = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.category.domain.model.Category> category = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.d9tilov.android.transaction.domain.model.Transaction>> transactions = null;
    
    @javax.inject.Inject
    public StatisticsDetailsViewModel(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.SavedStateHandle savedStateHandle, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.domain.contract.TransactionInteractor transactionInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.category.domain.model.Category> getCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.d9tilov.android.transaction.domain.model.Transaction>> getTransactions() {
        return null;
    }
}