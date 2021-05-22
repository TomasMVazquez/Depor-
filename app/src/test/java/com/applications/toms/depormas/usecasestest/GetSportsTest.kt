package com.applications.toms.depormas.usecasestest

import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.mockSport
import com.applications.toms.depormas.usecases.GetSports
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class GetSportsTest {

    @Mock
    lateinit var sportRepository: SportRepository

    lateinit var getSports: GetSports

    @Before
    fun setUp(){
        getSports = GetSports(sportRepository)
    }

    @Test
    fun `invoke calls sport repository`(){
        runBlocking {
            val sports = flowOf(listOf(mockSport.copy(id = 1)))
            whenever(sportRepository.getSports()).thenReturn(sports)

            val result = getSports.invoke()

            Assert.assertEquals(sports,result)
        }
    }

}