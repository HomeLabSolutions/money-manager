package com.d9tilov.moneymanager.profile.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.ProfileNavigator
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.goal.domain.GoalInteractor
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userInfoInteractor: UserInteractor,
    budgetInteractor: BudgetInteractor,
    regularTransactionInteractor: RegularTransactionInteractor,
    goalInteractor: GoalInteractor,
    billingInteractor: BillingInteractor
) : BaseViewModel<ProfileNavigator>() {

    val userData = userInfoInteractor.getCurrentUser()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserProfile.EMPTY)
    val budget = budgetInteractor.get()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BudgetData.EMPTY)
    val regularIncomes =
        regularTransactionInteractor.getAll(TransactionType.INCOME)
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val regularExpenses =
        regularTransactionInteractor.getAll(TransactionType.EXPENSE)
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val isPremium = billingInteractor.isPremium()
        .flowOn(Dispatchers.IO)
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    fun getCurrentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser
}
