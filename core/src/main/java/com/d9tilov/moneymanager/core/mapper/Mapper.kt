package com.d9tilov.moneymanager.core.mapper

import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID

interface Mapper<T, E> {
    fun toDataModel(model: T): E
    fun toDbModel(model: E, clientId: String = NO_ID.toString()): T
}
