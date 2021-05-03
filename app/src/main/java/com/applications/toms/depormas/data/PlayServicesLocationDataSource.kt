package com.applications.toms.depormas.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.applications.toms.depormas.data.source.LocationDataSource
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@SuppressLint("MissingPermission")
class PlayServicesLocationDataSource(context: Context) : LocationDataSource {

    private val geoCoder = Geocoder(context)
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


    override suspend fun findLastLocation(): Location =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result)
                }
        }

    override suspend fun findMyCurrentLocation(): Address =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result.toAddress())
                }
        }

    override suspend fun findLastRegion(): String? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result.toRegion())
                }
        }

    private fun Location?.toAddress(): Address {
        val addresses = this?.let {
            geoCoder.getFromLocation(latitude, longitude, 1)
        }
        return addresses!!.first()
    }

    private fun Location?.toRegion(): String? {
        val addresses = this?.let {
            geoCoder.getFromLocation(latitude, longitude, 1)
        }
        return addresses?.firstOrNull()?.countryCode
    }
}
