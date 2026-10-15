package com.d9tilov.android.transaction.data.impl

import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.transaction.domain.contract.NearbyPlacesRepo
import com.d9tilov.android.transaction.domain.model.NearbyPlace
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

class FirebaseNearbyPlacesRepo @Inject constructor() : NearbyPlacesRepo {
    override suspend fun getNearbyPlaces(location: LocationData): List<NearbyPlace> {
        val response =
            FirebaseFunctions
                .getInstance("us-central1")
                .getHttpsCallable("getNearbyPlaces")
                .call(
                    mapOf(
                        "latitude" to location.latitude,
                        "longitude" to location.longitude,
                        "language" to Locale.getDefault().language,
                    ),
                ).await()
        return parseNearbyPlaces(response.data)
    }
}

internal fun parseNearbyPlaces(data: Any?): List<NearbyPlace> {
    val places = (data as? Map<*, *>)?.get("places") as? List<*> ?: error("Invalid nearby places response")
    return places
        .map { item ->
            val place = item as? Map<*, *> ?: error("Invalid nearby place")
            val distance = (place["distanceMeters"] as? Number)?.toDouble() ?: error("Missing place distance")
            val id = place["id"] as? String ?: error("Missing place ID")
            val name = place["name"] as? String ?: error("Missing place name")
            check(id.isNotBlank() && name.isNotBlank() && distance.isFinite() && distance in 0.0..MAX_DISTANCE_METERS)
            NearbyPlace(
                id = id,
                name = name,
                address = place["address"] as? String ?: "",
                distanceMeters = distance.toInt(),
            )
        }.distinctBy(NearbyPlace::id)
        .sortedBy(NearbyPlace::distanceMeters)
        .take(MAX_NEARBY_PLACES)
}

private const val MAX_DISTANCE_METERS = 150.0
private const val MAX_NEARBY_PLACES = 20
