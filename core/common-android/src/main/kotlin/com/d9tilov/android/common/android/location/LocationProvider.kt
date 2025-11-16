package com.d9tilov.android.common.android.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.model.LocationData
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.tasks.await
import javax.inject.Named

interface LocationProvider {
    suspend fun getCurrentLocation(
        permissions: List<String> = listOf(Manifest.permission.ACCESS_COARSE_LOCATION),
    ): Flow<LocationData>
}

internal class LocationProviderImpl(
    val context: Context,
    @Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
) : LocationProvider {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    @RequiresPermission(
        anyOf = [
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ],
    )
    override suspend fun getCurrentLocation(permissions: List<String>): Flow<LocationData> {
        val freshLocationFlow =
            flow {
                val usePreciseLocation = permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)
                val priority =
                    if (usePreciseLocation) {
                        Priority.PRIORITY_HIGH_ACCURACY
                    } else {
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY
                    }
                val cancellationTokenSource = CancellationTokenSource()
                try {
                    val location: Location? =
                        fusedLocationClient
                            .getCurrentLocation(
                                priority,
                                cancellationTokenSource.token,
                            ).await()
                    emit(location?.let { LocationData(location.latitude, location.longitude) } ?: LocationData.EMPTY)
                } finally {
                    cancellationTokenSource.cancel()
                }
            }
        val lastKnownLocationFlow =
            flow {
                val location: Location? = fusedLocationClient.lastLocation.await()
                emit(location?.let { LocationData(location.latitude, location.longitude) } ?: LocationData.EMPTY)
            }
        return merge(freshLocationFlow, lastKnownLocationFlow)
            .flowOn(ioDispatcher)
            .catch { emit(LocationData.EMPTY) }
    }
}
