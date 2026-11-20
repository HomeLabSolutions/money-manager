package com.d9tilov.android.places.di

import com.d9tilov.android.places.domain.contract.NearbyPlacesInteractor
import com.d9tilov.android.places.domain.impl.NearbyPlacesInteractorImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface PlacesDomainModule {
    @Binds
    val NearbyPlacesInteractorImpl.provideNearbyPlacesInteractor: NearbyPlacesInteractor
}
