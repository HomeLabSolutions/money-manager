package com.d9tilov.android.common.android.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import timber.log.Timber

data class LocationData(
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        val EMPTY = LocationData(0.0, 0.0)
    }
}

interface LocationProvider {
    suspend fun getCurrentLocation(
        permissions: List<String> = listOf(Manifest.permission.ACCESS_COARSE_LOCATION),
    ): LocationData
}

internal class LocationProviderImpl(
    val context: Context,
) : LocationProvider {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    @RequiresPermission(
        anyOf = [
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ],
    )
    override suspend fun getCurrentLocation(permissions: List<String>): LocationData =
        try {
            val usePreciseLocation = permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)
            val priority =
                if (usePreciseLocation) {
                    Priority.PRIORITY_HIGH_ACCURACY
                } else {
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY
                }
            val location: Location? =
                fusedLocationClient
                    .getCurrentLocation(
                        priority,
                        CancellationTokenSource().token,
                    ).await()
            location?.let {
                LocationData(
                    latitude = it.latitude,
                    longitude = it.longitude,
                )
            } ?: LocationData.EMPTY
        } catch (ex: IllegalArgumentException) {
            Timber.tag(TAG).e("Unable to get current location: $ex")
            LocationData.EMPTY
        } catch (ex: SecurityException) {
            Timber.tag(TAG).e("Unable to get current location because of permissions: $ex")
            LocationData.EMPTY
        }
}
