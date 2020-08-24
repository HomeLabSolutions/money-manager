package com.d9tilov.moneymanager.accumulations.data

import com.d9tilov.moneymanager.accumulations.data.entity.Accumulation
import com.d9tilov.moneymanager.accumulations.data.local.AccumulationSource
import com.d9tilov.moneymanager.accumulations.domain.AccumulationRepo

class AccumulationDataRepo(private val accumulationSource: AccumulationSource) : AccumulationRepo {

    override fun insert(accumulation: Accumulation) = accumulationSource.insert(accumulation)

    override fun update(accumulation: Accumulation) = accumulationSource.update(accumulation)
}
