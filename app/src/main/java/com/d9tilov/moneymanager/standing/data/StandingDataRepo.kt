package com.d9tilov.moneymanager.standing.data

import com.d9tilov.moneymanager.standing.data.entity.StandingData
import com.d9tilov.moneymanager.standing.data.local.StandingSource
import com.d9tilov.moneymanager.standing.domain.StandingRepo

class StandingDataRepo(private val standingSource: StandingSource) : StandingRepo {

    override fun insert(standingData: StandingData) = standingSource.insert(standingData)

    override fun update(standingData: StandingData) = standingSource.update(standingData)

    override fun delete(standingData: StandingData) = standingSource.delete(standingData)
}
