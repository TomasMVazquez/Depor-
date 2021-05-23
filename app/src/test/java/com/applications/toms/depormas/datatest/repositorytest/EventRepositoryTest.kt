package com.applications.toms.depormas.datatest.repositorytest

import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.data.source.events.LocalEventDataSource
import com.applications.toms.depormas.data.source.events.RemoteEventDataSource
import com.applications.toms.depormas.mockEvent
import com.applications.toms.depormas.mockFirestore
import com.google.firebase.firestore.CollectionReference
import com.nhaarman.mockitokotlin2.verify
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
class EventRepositoryTest {

    @Mock
    lateinit var localEventDataSource: LocalEventDataSource

    @Mock
    lateinit var remoteEventDataSource: RemoteEventDataSource

    lateinit var eventRepository: EventRepository

    @Before
    fun setUp(){
        eventRepository = EventRepository(localEventDataSource,remoteEventDataSource)
    }

    @Test
    fun `getEvents gets from local data source and save remote data`(){
        runBlocking {
            val events = listOf(mockEvent.copy(id = "1"))
            val flowEvents = flowOf(events)
            whenever(remoteEventDataSource.getEventList()).thenReturn(events)
            whenever(localEventDataSource.getAllEvent()).thenReturn(flowEvents)

            val result = eventRepository.getEvents()

            Assert.assertEquals(flowEvents,result)
        }
    }

    @Test
    fun `saveEvent getting the id from remote`(){
        runBlocking {
            val stringID = "01TestId01"
            val event = mockEvent.copy(id = "")
            whenever(remoteEventDataSource.getEventDocumentId()).thenReturn("01TestId01")

            val result = eventRepository.saveEvent(event)

            Assert.assertEquals(stringID,result)
        }
    }

    @Test
    fun `updateEvent in the remote`(){
        runBlocking {
            val event = mockEvent.copy(id = "")

            eventRepository.updateEvent(event)

            verify(remoteEventDataSource).updateEvent(event)
        }
    }

}