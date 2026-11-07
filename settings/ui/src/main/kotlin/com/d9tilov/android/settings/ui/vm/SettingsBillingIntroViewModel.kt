package com.d9tilov.android.settings.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.core.constants.DataConstants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsBillingIntroViewModel
    @Inject
    constructor(
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
