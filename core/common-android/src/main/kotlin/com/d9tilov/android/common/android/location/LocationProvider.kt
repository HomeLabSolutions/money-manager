package com.d9tilov.android.common.android.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

data class LocationData(
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        val EMPTY = LocationData(0.0, 0.0)
    }
}

interface LocationProvider {
    suspend fun getCurrentLocation(): LocationData
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
    override suspend fun getCurrentLocation(): LocationData {
        val location: Location? =
            fusedLocationClient
                .getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    CancellationTokenSource().token,
                ).await()
        return location?.let {
            LocationData(
                latitude = it.latitude,
                longitude = it.longitude,
            )
        } ?: LocationData.EMPTY
    }
}
