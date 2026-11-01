package com.d9tilov.android.widget.domain.contract

import com.d9tilov.android.widget.domain.model.SpendingWidgetState
import kotlinx.coroutines.flow.Flow

interface SpendingWidgetInteractor {
    fun observe(uid: String?): Flow<SpendingWidgetState>
}
