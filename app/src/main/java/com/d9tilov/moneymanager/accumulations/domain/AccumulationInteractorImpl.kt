package com.d9tilov.moneymanager.accumulations.domain

import com.d9tilov.moneymanager.accumulations.data.entity.Accumulation

class AccumulationInteractorImpl(private val accumulationRepo: AccumulationRepo) :
    AccumulationInteractor {

    override fun insert(accumulation: Accumulation) = accumulationRepo.insert(accumulation)

    override fun update(accumulation: Accumulation) = accumulationRepo.update(accumulation)
}
