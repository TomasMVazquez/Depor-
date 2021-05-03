package com.applications.toms.depormas.usecases

import com.applications.toms.depormas.data.repository.LocationRepository

class GetMyLocation(private val locationRepository: LocationRepository) {
    suspend fun getLocation() = locationRepository.findLastLocation()
    suspend fun getRegion() = locationRepository.findLastRegion()
}