package com.d9tilov.android.places.di

import com.d9tilov.android.places.domain.contract.NearbyPlacesInteractor
import com.d9tilov.android.places.domain.impl.NearbyPlacesInteractorImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val placesDomainModule =
    module {
        factory<NearbyPlacesInteractorImpl>() bind NearbyPlacesInteractor::class
    }
