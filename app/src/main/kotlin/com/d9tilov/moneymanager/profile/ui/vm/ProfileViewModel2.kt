package com.d9tilov.moneymanager.profile.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.currency.data.contract.model.CurrencyMetaData
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.moneymanager.base.ui.navigator.ProfileNavigator
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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

sealed class ProfileUiItem {
    data class CurrencyUiItem(val currencyCode: String = DEFAULT_CURRENCY_CODE) :
        ProfileUiItem()

    data class BudgetUiItem(val budgetData: BudgetData = BudgetData.EMPTY) : ProfileUiItem()
    data class RegularIncomeUiItem(val regularIncomes: List<RegularTransaction> = emptyList()) :
        ProfileUiItem()

    data class RegularExpenseUiItem(val regularExpenses: List<RegularTransaction> = emptyList()) :
        ProfileUiItem()

    object Goals : ProfileUiItem()
    data class Settings(val isPremium: Boolean = false) : ProfileUiItem()
}

data class ProfileUiState(
    val userProfile: UserProfile? = null,
    val currency: ProfileUiItem = ProfileUiItem.CurrencyUiItem(),
    val budgetData: ProfileUiItem = ProfileUiItem.BudgetUiItem(),
    val regularIncomes: ProfileUiItem = ProfileUiItem.RegularIncomeUiItem(),
    val regularExpenses: ProfileUiItem = ProfileUiItem.RegularExpenseUiItem(),
    val settings: ProfileUiItem = ProfileUiItem.Settings(),
    val showLogoutDialog: Boolean = false
)

@HiltViewModel
class ProfileViewModel2 @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val userInfoInteractor: UserInteractor,
    currencyInteractor: com.d9tilov.android.currency.domain.contract.CurrencyInteractor,
    budgetInteractor: BudgetInteractor,
    regularTransactionInteractor: RegularTransactionInteractor,
    billingInteractor: BillingInteractor,
) : BaseViewModel<ProfileNavigator>() {

    private val _showDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    val userCurrencyPair = combine(
        userInfoInteractor.getCurrentUser(),
        currencyInteractor.getMainCurrencyFlow()
    ) { userInfo, currency ->
        Pair(userInfo, currency)
    }
    val profileState: StateFlow<ProfileUiState> =
        combine(
            userCurrencyPair,
            budgetInteractor.get(),
            regularTransactionInteractor.getAll(TransactionType.INCOME),
            regularTransactionInteractor.getAll(TransactionType.EXPENSE),
            billingInteractor.isPremium(),
        ) { userCurrencyPair, budgetData, regularIncomeList, regularExpenseList, isPremium ->
            val userInfo = userCurrencyPair.first
            val currency: CurrencyMetaData = userCurrencyPair.second
            userInfo?.let { user ->
                ProfileUiState(
                    userProfile = user,
                    currency = ProfileUiItem.CurrencyUiItem(currency.code),
                    budgetData = ProfileUiItem.BudgetUiItem(budgetData),
                    regularIncomes = ProfileUiItem.RegularIncomeUiItem(regularIncomeList),
                    regularExpenses = ProfileUiItem.RegularExpenseUiItem(regularExpenseList),
                    settings = ProfileUiItem.Settings(isPremium)
                )
            } ?: ProfileUiState()
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
