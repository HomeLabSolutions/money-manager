package com.d9tilov.moneymanager.profile.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userInfoInteractor: UserInteractor,
    budgetInteractor: BudgetInteractor,
    regularTransactionInteractor: RegularTransactionInteractor,
    goalInteractor: GoalInteractor,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<ProfileNavigator>() {

    private val userData = MutableLiveData<UserProfile>()
    private val budget = MutableLiveData<BudgetData>()
    private val regularIncomes = MutableLiveData<List<RegularTransaction>>()
    private val regularExpenses = MutableLiveData<List<RegularTransaction>>()
    private val goals = MutableLiveData<List<Goal>>()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val job = Job()

    init {
        viewModelScope.launch(Dispatchers.Main + job) {
            userInfoInteractor.getCurrentUser()
                .flowOn(Dispatchers.IO)
                .collect { userData.value = it }
        }
        viewModelScope.launch(Dispatchers.Main + job) {
            budgetInteractor.get()
                .flowOn(Dispatchers.IO)
                .collect { budget.value = it }
        }
        viewModelScope.launch(Dispatchers.Main + job) {
            regularTransactionInteractor.getAll(TransactionType.INCOME)
                .flowOn(Dispatchers.IO)
                .collect { regularIncomes.value = it }
        }
        viewModelScope.launch(Dispatchers.Main + job) {
            regularTransactionInteractor.getAll(TransactionType.EXPENSE)
                .flowOn(Dispatchers.IO)
                .collect { regularExpenses.value = it }
        }
        viewModelScope.launch(Dispatchers.Main + job) {
            goalInteractor.getAll()
                .flowOn(Dispatchers.IO)
                .collect { goals.value = it }
        }
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun logout() {
        job.cancel()
        viewModelScope.launch(Dispatchers.IO) { userInfoInteractor.deleteUser() }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param(
                FirebaseAnalytics.Param.ITEM_CATEGORY,
                "logout"
            )
        }
    }

    fun userData(): LiveData<UserProfile> = userData
    fun budget(): LiveData<BudgetData> = budget
    fun regularIncomes(): LiveData<List<RegularTransaction>> = regularIncomes
    fun regularExpenses(): LiveData<List<RegularTransaction>> = regularExpenses
    fun goals(): LiveData<List<Goal>> = goals
}
