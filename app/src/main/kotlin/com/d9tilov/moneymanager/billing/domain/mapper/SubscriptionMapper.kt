package com.d9tilov.moneymanager.billing.domain.mapper

import com.d9tilov.moneymanager.billing.domain.entity.PurchaseRemote
import com.d9tilov.moneymanager.billing.domain.entity.PurchaseMetaData

fun PurchaseRemote.toPurchaseMetaData() =
    PurchaseMetaData(
        purchaseTime = purchaseTime ?: 0L,
        autoRenewing = autoRenewing ?: false,
        acknowledged = acknowledged ?: false
    )
