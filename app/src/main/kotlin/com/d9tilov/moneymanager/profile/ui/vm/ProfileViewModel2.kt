package com.d9tilov.moneymanager.profile.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.ProfileNavigator
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class ProfileUiItem {
    data class CurrencyUiItem(val currencyCode: String = DataConstants.DEFAULT_CURRENCY_CODE) :
        ProfileUiItem()

    data class BudgetUiItem(val budgetData: BudgetData = BudgetData.EMPTY) : ProfileUiItem()
    data class RegularIncomeUiItem(val regularIncomes: List<RegularTransaction> = emptyList()) :
        ProfileUiItem()

    data class RegularExpenseUiItem(val regularExpenses: List<RegularTransaction> = emptyList()) :
        ProfileUiItem()

    object Goals : ProfileUiItem()
    object Settings : ProfileUiItem()
}

data class ProfileUiState(
    val userProfile: UserProfile? = null,
    val currency: ProfileUiItem = ProfileUiItem.CurrencyUiItem(),
    val budgetData: ProfileUiItem = ProfileUiItem.BudgetUiItem(),
    val regularIncomes: ProfileUiItem = ProfileUiItem.RegularIncomeUiItem(),
    val regularExpenses: ProfileUiItem = ProfileUiItem.RegularExpenseUiItem(),
    val isPremium: Boolean = false,
    val showLogoutDialog: Boolean = false
)

@HiltViewModel
class ProfileViewModel2 @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val userInfoInteractor: UserInteractor,
    budgetInteractor: BudgetInteractor,
    regularTransactionInteractor: RegularTransactionInteractor,
    billingInteractor: BillingInteractor,
) : BaseViewModel<ProfileNavigator>() {

    private val _showDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    val profileState: StateFlow<ProfileUiState> =
        combine(
            userInfoInteractor.getCurrentUser(),
            budgetInteractor.get(),
            regularTransactionInteractor.getAll(TransactionType.INCOME),
            regularTransactionInteractor.getAll(TransactionType.EXPENSE),
            billingInteractor.isPremium(),
        ) { userInfo, budgetData, regularIncomeList, regularExpenseList, isPremium ->
            ProfileUiState(
                userProfile = userInfo,
                currency = ProfileUiItem.CurrencyUiItem(
                    userInfo?.currentCurrencyCode ?: DataConstants.DEFAULT_CURRENCY_CODE
                ),
                budgetData = ProfileUiItem.BudgetUiItem(budgetData),
                regularIncomes = ProfileUiItem.RegularIncomeUiItem(regularIncomeList),
                regularExpenses = ProfileUiItem.RegularExpenseUiItem(regularExpenseList),
                isPremium = isPremium
            )
        }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ProfileUiState()
            )

    fun showDialog() {
        _showDialog.value = true
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

    fun logout(navigateCallback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userInfoInteractor.deleteUser()
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                param(FirebaseAnalytics.Param.ITEM_CATEGORY, "logout")
            }
            withContext(Dispatchers.Main) { navigateCallback.invoke() }
        }
    }

    fun getCurrentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser
}
