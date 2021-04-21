package com.applications.toms.depormas.data.source

import android.location.Address
import android.location.Location

interface LocationDataSource {
    suspend fun findLastLocation(): Location
    suspend fun findMyCurrentLocation(): Address
}