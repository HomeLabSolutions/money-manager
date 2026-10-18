package com.d9tilov.android.places.di

import com.d9tilov.android.places.data.impl.NearbyPlacesDataRepo
import com.d9tilov.android.places.domain.contract.NearbyPlacesRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PlacesDataModule {
    @Binds
    fun provideNearbyPlacesRepo(impl: NearbyPlacesDataRepo): NearbyPlacesRepo
}
