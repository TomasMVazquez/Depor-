package com.applications.toms.depormas.data.repository

import android.util.Log
import com.applications.toms.depormas.data.source.LocationDataSource
import com.applications.toms.depormas.data.source.PermissionChecker
import com.applications.toms.depormas.data.source.PermissionChecker.*

class LocationRepository(
        private val locationDataSource: LocationDataSource,
        private val permissionChecker: PermissionChecker
) {

    suspend fun findLastLocation(): Map<String,Double> {
        return when {
            permissionChecker.check(Permission.FINE_LOCATION) -> {
                try {
                    mapOf(
                        "latitude" to locationDataSource.findMyCurrentLocation().latitude,
                        "longitude" to locationDataSource.findMyCurrentLocation().longitude
                    )
                }catch (t: Throwable){
                    Log.d("Tomas", "findLastLocation: ${t.message}")
                    DEFAULT_LOCATION
                }
            }
            permissionChecker.check(Permission.COARSE_LOCATION) -> {
                try {
                    mapOf(
                        "latitude" to locationDataSource.findLastLocation().latitude,
                        "longitude" to locationDataSource.findLastLocation().longitude
                    )
                }catch (t: Throwable){
                    Log.d("Tomas", "findLastLocation: ${t.message}")
                    DEFAULT_LOCATION
                }
            }
            else -> DEFAULT_LOCATION
        }
    }

    suspend fun findLastRegion(): String {
        return if (permissionChecker.check(Permission.COARSE_LOCATION) || permissionChecker.check(Permission.FINE_LOCATION)) {
            locationDataSource.findLastRegion() ?: DEFAULT_REGION
        } else {
            DEFAULT_REGION
        }
    }

    companion object{
        private val DEFAULT_LOCATION = mapOf(
                "latitude" to 0.0,
                "longitude" to 0.0
        )
        private const val DEFAULT_REGION = ""
    }
}