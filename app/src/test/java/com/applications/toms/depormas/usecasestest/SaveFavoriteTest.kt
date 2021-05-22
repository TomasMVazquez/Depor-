package com.applications.toms.depormas.usecasestest

import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.data.repository.FavoriteRepository
import com.applications.toms.depormas.mockEvent
import com.applications.toms.depormas.mockFavorite
import com.applications.toms.depormas.usecases.SaveFavorite
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class SaveFavoriteTest {

    @Mock
    lateinit var favoriteRepository: FavoriteRepository

    @Mock
    lateinit var eventRepository: EventRepository

    lateinit var saveFavorite: SaveFavorite

    @Before
    fun setUp(){
        saveFavorite = SaveFavorite(favoriteRepository, eventRepository)
    }

    @Test
    fun `invoke save favorite in the repository`(){
        runBlocking {
            val event = mockEvent.copy("0123")
            val favorite = mockFavorite.copy(0,event.id)

            saveFavorite.invoke(event)

            verify(eventRepository).updateEvent(event)
            verify(favoriteRepository).saveNewFavorite(favorite)
        }
    }
}