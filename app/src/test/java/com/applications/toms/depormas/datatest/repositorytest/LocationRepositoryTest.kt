package com.applications.toms.depormas.datatest.repositorytest

import android.location.Address
import android.location.Location
import com.applications.toms.depormas.data.repository.LocationRepository
import com.applications.toms.depormas.data.source.LocationDataSource
import com.applications.toms.depormas.data.source.PermissionChecker
import com.applications.toms.depormas.data.source.PermissionChecker.Permission.*
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocationRepositoryTest {

    @Mock
    lateinit var locationDataSource: LocationDataSource

    @Mock
    lateinit var permissionChecker: PermissionChecker

    @Mock
    lateinit var location: Location

    @Mock
    lateinit var address: Address

    lateinit var locationRepository: LocationRepository

    @Before
    fun setUp(){
        locationRepository = LocationRepository(locationDataSource,permissionChecker)
    }

    @Test
    fun `return default location when permission not granted`(){
        runBlocking {
            whenever(permissionChecker.check(COARSE_LOCATION)).thenReturn(false)
            whenever(permissionChecker.check(FINE_LOCATION)).thenReturn(false)

            val location = locationRepository.findLastLocation()

            assertEquals(LocationRepository.DEFAULT_LOCATION,location)
        }
    }

    @Test
    fun `return default region when permission not granted`(){
        runBlocking {
            whenever(permissionChecker.check(COARSE_LOCATION)).thenReturn(false)
            whenever(permissionChecker.check(FINE_LOCATION)).thenReturn(false)

            val region = locationRepository.findLastRegion()

            assertEquals(LocationRepository.DEFAULT_REGION,region)
        }
    }

    @Test
    fun `return address when fine permission is granted`(){
        runBlocking {
            val aLocation = mapOf("latitude" to 1.2, "longitude" to 2.1)
            whenever(permissionChecker.check(FINE_LOCATION)).thenReturn(true)
            whenever(address.latitude).thenReturn(1.2)
            whenever(address.longitude).thenReturn(2.1)
            whenever(locationDataSource.findMyCurrentLocation()).thenReturn(address)

            val location = locationRepository.findLastLocation()

            assertEquals(aLocation,location)
        }
    }

    @Test
    fun `return location when coarse permission is granted`(){
        runBlocking {
            val aLocation = mapOf("latitude" to 1.2, "longitude" to 2.1)
            whenever(permissionChecker.check(COARSE_LOCATION)).thenReturn(true)
            whenever(location.latitude).thenReturn(1.2)
            whenever(location.longitude).thenReturn(2.1)
            whenever(locationDataSource.findLastLocation()).thenReturn(location)

            val location = locationRepository.findLastLocation()

            assertEquals(aLocation,location)
        }
    }

    @Test
    fun `return region when permission is granted`(){
        runBlocking {
            val aRegion = "ES"
            whenever(permissionChecker.check(FINE_LOCATION)).thenReturn(true)
            whenever(locationDataSource.findLastRegion()).thenReturn(aRegion)

            val region = locationRepository.findLastRegion()

            assertEquals(aRegion,region)
        }
    }
}