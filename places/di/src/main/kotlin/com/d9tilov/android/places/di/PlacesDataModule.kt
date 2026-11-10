package com.d9tilov.android.places.di

import com.d9tilov.android.places.data.contract.NearbyPlacesRemoteSource
import com.d9tilov.android.places.data.impl.GeoapifyNearbyPlacesRemoteSource
import com.d9tilov.android.places.data.impl.NearbyPlacesDataRepo
import com.d9tilov.android.places.domain.contract.NearbyPlacesRepo
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val placesDataModule =
    module {
        factory<GeoapifyNearbyPlacesRemoteSource>() bind NearbyPlacesRemoteSource::class
        factory<NearbyPlacesDataRepo>() bind NearbyPlacesRepo::class
    }
