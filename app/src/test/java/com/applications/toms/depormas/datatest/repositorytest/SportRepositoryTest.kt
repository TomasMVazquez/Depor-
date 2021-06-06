package com.applications.toms.depormas.datatest.repositorytest

import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.data.source.sports.LocalSportDataSource
import com.applications.toms.depormas.data.source.sports.RemoteSportDataSource
import com.applications.toms.depormas.mockSport
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
class SportRepositoryTest {

    @Mock
    lateinit var localSportDataSource: LocalSportDataSource

    @Mock
    lateinit var remoteSportDataSource: RemoteSportDataSource

    lateinit var sportRepository: SportRepository

    @Before
    fun setUp(){
        sportRepository = SportRepository(localSportDataSource,remoteSportDataSource)
    }

    @Test
    fun `getSports gets from local after sending data from remote`(){
        runBlocking {
            val sports = listOf(mockSport.copy(id = 0))
            val flowSports = flowOf(sports)

            whenever(localSportDataSource.isEmpty()).thenReturn(true)
            whenever(remoteSportDataSource.getSportsCollection()).thenReturn(flowSports)
            whenever(localSportDataSource.getAllSports()).thenReturn(flowSports)

            val result = sportRepository.getSports()

            Assert.assertEquals(flowSports,result)
        }
    }

}