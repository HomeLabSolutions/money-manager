package com.d9tilov.moneymanager.profile.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.ProfileNavigator
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.goal.domain.GoalInteractor
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userInfoInteractor: UserInteractor,
    budgetInteractor: BudgetInteractor,
    regularTransactionInteractor: RegularTransactionInteractor,
    goalInteractor: GoalInteractor
) : BaseViewModel<ProfileNavigator>() {

    private val userData: MutableStateFlow<UserProfile?> = MutableStateFlow(UserProfile.EMPTY)
    private val budget = MutableStateFlow(BudgetData.EMPTY)
    private val regularIncomes = MutableStateFlow<List<RegularTransaction>>(emptyList())
    private val regularExpenses = MutableStateFlow<List<RegularTransaction>>(emptyList())
    private val goals = MutableStateFlow<List<Goal>>(emptyList())

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                userInfoInteractor.getCurrentUser()
                    .collect { userData.value = it }
            }
            launch {
                budgetInteractor.get()
                    .collect { budget.value = it }
            }
            launch {
                regularTransactionInteractor.getAll(TransactionType.INCOME)
                    .collect { regularIncomes.value = it }
            }
            launch {
                regularTransactionInteractor.getAll(TransactionType.EXPENSE)
                    .collect { regularExpenses.value = it }
            }
            launch {
                goalInteractor.getAll()
                    .collect { goals.value = it }
            }
        }
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun userData(): StateFlow<UserProfile?> = userData
    fun budget(): StateFlow<BudgetData> = budget
    fun regularIncomes(): StateFlow<List<RegularTransaction>> = regularIncomes
    fun regularExpenses(): StateFlow<List<RegularTransaction>> = regularExpenses
    fun goals(): StateFlow<List<Goal>> = goals
}
