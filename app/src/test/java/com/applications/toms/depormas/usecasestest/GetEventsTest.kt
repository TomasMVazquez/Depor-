package com.applications.toms.depormas.usecasestest

import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.mockEvent
import com.applications.toms.depormas.usecases.GetEvents
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.nhaarman.mockitokotlin2.whenever

@RunWith(MockitoJUnitRunner::class)
class GetEventsTest {

    @Mock
    lateinit var eventRepository:EventRepository

    lateinit var getEvents: GetEvents

    @Before
    fun setUp(){
        getEvents = GetEvents(eventRepository)
    }

    @Test
    fun `invoke calls event repository`(){
        runBlocking {
            val events = flowOf(listOf(mockEvent.copy(id = "1")))
            whenever(eventRepository.getEvents()).thenReturn(events)

            val result = getEvents.invoke()

            Assert.assertEquals(events,result)
        }
    }

}