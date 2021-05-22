package com.applications.toms.depormas.usecasestest

import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.data.repository.FavoriteRepository
import com.applications.toms.depormas.mockEvent
import com.applications.toms.depormas.mockFavorite
import com.applications.toms.depormas.usecases.SaveEvent
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever

@RunWith(MockitoJUnitRunner::class)
class SaveEventTest {

    @Mock
    lateinit var favoriteRepository: FavoriteRepository

    @Mock
    lateinit var eventRepository: EventRepository

    lateinit var saveEvent:SaveEvent

    @Before
    fun setUp(){
        saveEvent = SaveEvent(eventRepository, favoriteRepository)
    }

    @Test
    fun `invoke calls to save event in the repository`(){
        runBlocking {
            val event = mockEvent.copy("0123")
            val favorite = mockFavorite.copy(0,event.id)

            whenever(eventRepository.saveEvent(event)).thenReturn(event.id)
            saveEvent.invoke(event)

            verify(eventRepository).saveEvent(event)
            verify(favoriteRepository).saveNewFavorite(favorite)
        }
    }

}