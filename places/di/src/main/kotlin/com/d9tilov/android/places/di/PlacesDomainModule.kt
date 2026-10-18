package com.d9tilov.android.places.di

import com.d9tilov.android.places.domain.contract.NearbyPlacesInteractor
import com.d9tilov.android.places.domain.impl.NearbyPlacesInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PlacesDomainModule {
    @Binds
    fun provideNearbyPlacesInteractor(impl: NearbyPlacesInteractorImpl): NearbyPlacesInteractor
}
