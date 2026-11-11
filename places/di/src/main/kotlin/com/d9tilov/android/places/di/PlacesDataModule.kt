package com.d9tilov.android.places.di

import com.d9tilov.android.places.data.contract.NearbyPlacesRemoteSource
import com.d9tilov.android.places.data.impl.GeoapifyNearbyPlacesRemoteSource
import com.d9tilov.android.places.data.impl.NearbyPlacesDataRepo
import com.d9tilov.android.places.domain.contract.NearbyPlacesRepo
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface PlacesDataModule {
    @Binds
    val GeoapifyNearbyPlacesRemoteSource.provideNearbyPlacesRemoteSource: NearbyPlacesRemoteSource

    @Binds
    val NearbyPlacesDataRepo.provideNearbyPlacesRepo: NearbyPlacesRepo
}
