package com.applications.toms.depormas.data.repository

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
                mapOf(
                        "latitude" to locationDataSource.findMyCurrentLocation().latitude,
                        "longitude" to locationDataSource.findMyCurrentLocation().longitude
                )
            }
            permissionChecker.check(Permission.COARSE_LOCATION) -> {
                mapOf(
                        "latitude" to locationDataSource.findLastLocation().latitude,
                        "longitude" to locationDataSource.findLastLocation().longitude
                )
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
        val DEFAULT_LOCATION = mapOf(
                "latitude" to 0.0,
                "longitude" to 0.0
        )
        const val DEFAULT_REGION = ""
    }
}