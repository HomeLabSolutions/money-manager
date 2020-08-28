package com.d9tilov.moneymanager.prepopulate.domain

class PrepopulateInteractorImpl(private val prepopulateRepo: PrepopulateRepo) :
    PrepopulateInteractor {

    override fun showPrepopulate() = prepopulateRepo.showPrepopulate()
}
