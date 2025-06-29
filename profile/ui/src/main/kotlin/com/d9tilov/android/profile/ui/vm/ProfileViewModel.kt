package com.d9tilov.android.profile.ui.vm

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.analytics.model.AnalyticsEvent
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.budget.domain.model.BudgetData
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import com.d9tilov.android.user.domain.contract.UserInteractor
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
    data class CurrencyUiItem(
        val currencyCode: String = DEFAULT_CURRENCY_CODE,
    ) : ProfileUiItem()

    data class BudgetUiItem(
        val budgetData: BudgetData = BudgetData.EMPTY,
    ) : ProfileUiItem()

    data class RegularIncomeUiItem(
        val regularIncomes: List<RegularTransaction> = emptyList(),
    ) : ProfileUiItem()

    data class RegularExpenseUiItem(
        val regularExpenses: List<RegularTransaction> = emptyList(),
    ) : ProfileUiItem()

    data object Goals : ProfileUiItem()

    data class Settings(
        val isPremium: Boolean = false,
    ) : ProfileUiItem()
}

data class UserUiProfile(
    val photo: Uri? = null,
    val name: String? = null,
)

data class ProfileUiState(
    val userProfile: UserUiProfile = UserUiProfile(),
    val currency: ProfileUiItem = ProfileUiItem.CurrencyUiItem(),
    val budgetData: ProfileUiItem = ProfileUiItem.BudgetUiItem(),
    val regularIncomes: ProfileUiItem = ProfileUiItem.RegularIncomeUiItem(),
    val regularExpenses: ProfileUiItem = ProfileUiItem.RegularExpenseUiItem(),
    val goals: ProfileUiItem = ProfileUiItem.Goals,
    val settings: ProfileUiItem = ProfileUiItem.Settings(),
    val showLogoutDialog: Boolean = false,
)

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val analyticsSender: AnalyticsSender,
        private val userInfoInteractor: UserInteractor,
        currencyInteractor: CurrencyInteractor,
        budgetInteractor: BudgetInteractor,
        regularTransactionInteractor: RegularTransactionInteractor,
        billingInteractor: BillingInteractor,
    ) : ViewModel() {
        private val _showDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
        private val userCurrencyPair =
            combine(
                userInfoInteractor.getCurrentUser(),
                currencyInteractor.getMainCurrencyFlow(),
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
                    val photoUri: Uri? = if (user.photoUrl != null) user.photoUrl?.toUri() else null
                    ProfileUiState(
                        userProfile = UserUiProfile(photoUri, user.displayedName),
                        currency = ProfileUiItem.CurrencyUiItem(currency.code),
                        budgetData = ProfileUiItem.BudgetUiItem(budgetData),
                        regularIncomes = ProfileUiItem.RegularIncomeUiItem(regularIncomeList),
                        regularExpenses = ProfileUiItem.RegularExpenseUiItem(regularExpenseList),
                        settings = ProfileUiItem.Settings(isPremium),
                    )
                } ?: ProfileUiState()
            }.flowOn(Dispatchers.IO)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = ProfileUiState(),
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
                analyticsSender.send(AnalyticsEvent.Client.Auth.Logout)
                withContext(Dispatchers.Main) { navigateCallback() }
            }
        }
    }
