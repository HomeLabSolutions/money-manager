package com.d9tilov.android.settings.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.core.constants.DataConstants.TAG
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

@ViewModelKey
@ContributesIntoMap(AppScope::class, binding<ViewModel>())
@Inject
class SettingsBillingIntroViewModel(
    private val billingInteractor: BillingInteractor,
) : ViewModel() {
    private val skuExceptionHandler =
        CoroutineExceptionHandler { _, _ ->
            Timber.tag(TAG).d("Unable to get sku")
        }

    val skuDetails =
        billingInteractor
            .getSkuDetails()
            .flowOn(Dispatchers.IO + skuExceptionHandler)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val purchaseCompleted =
        billingInteractor.isNewPurchaseAcknowledged
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, false)
}
