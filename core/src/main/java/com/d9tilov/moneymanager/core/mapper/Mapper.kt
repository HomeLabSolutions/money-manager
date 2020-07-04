package com.d9tilov.moneymanager.core.mapper

interface Mapper<T, E> {
    fun toDataModel(model: T): E
    fun toDbModel(model: E): T
}
