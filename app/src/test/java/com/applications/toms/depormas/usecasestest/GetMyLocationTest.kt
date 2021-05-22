package com.applications.toms.depormas.usecasestest

import com.applications.toms.depormas.data.repository.LocationRepository
import com.applications.toms.depormas.usecases.GetMyLocation
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.nhaarman.mockitokotlin2.whenever

@RunWith(MockitoJUnitRunner::class)
class GetMyLocationTest {

    @Mock
    lateinit var locationRepository: LocationRepository

    lateinit var getMyLocation: GetMyLocation

    @Before
    fun setUp(){
        getMyLocation = GetMyLocation(locationRepository)
    }

    @Test
    fun `get location from repository`(){
        runBlocking {
            val defaultLocation = mapOf("latitude" to 0.0, "longitude" to 0.0)
            whenever(locationRepository.findLastLocation()).thenReturn(defaultLocation)

            val result = getMyLocation.getLocation()

            Assert.assertEquals(defaultLocation,result)
        }
    }

    @Test
    fun `get region from repository`(){
        runBlocking {
            whenever(locationRepository.findLastRegion()).thenReturn("ES")

            val result = getMyLocation.getRegion()

            Assert.assertEquals("ES",result)
        }
    }

}