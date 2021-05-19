package com.d9tilov.moneymanager.profile.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
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
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userInfoInteractor: UserInteractor,
    budgetInteractor: BudgetInteractor,
    regularTransactionInteractor: RegularTransactionInteractor,
    goalInteractor: GoalInteractor,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<ProfileNavigator>() {

    val userData: LiveData<UserProfile> = userInfoInteractor.getCurrentUser().asLiveData()
    val budget: LiveData<BudgetData> = budgetInteractor.get().asLiveData()
    val regularIncomes: LiveData<List<RegularTransaction>> =
        regularTransactionInteractor.getAll(TransactionType.INCOME).asLiveData()
    val regularExpenses: LiveData<List<RegularTransaction>> =
        regularTransactionInteractor.getAll(TransactionType.EXPENSE).asLiveData()
    val goals: LiveData<List<Goal>> = goalInteractor.getAll().asLiveData()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun logout() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param(
                FirebaseAnalytics.Param.ITEM_CATEGORY,
                "logout"
            )
        }
    }
}
