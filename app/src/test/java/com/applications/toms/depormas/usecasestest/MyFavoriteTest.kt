package com.applications.toms.depormas.usecasestest

import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.data.repository.FavoriteRepository
import com.applications.toms.depormas.mockEvent
import com.applications.toms.depormas.mockFavorite
import com.applications.toms.depormas.usecases.MyFavorite
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever

@RunWith(MockitoJUnitRunner::class)
class MyFavoriteTest {

    @Mock
    lateinit var favoriteRepository: FavoriteRepository

    @Mock
    lateinit var eventRepository: EventRepository

    lateinit var myFavorite: MyFavorite

    @Before
    fun setUp(){
        myFavorite = MyFavorite(favoriteRepository, eventRepository)
    }

    @Test
    fun `get favorites from repository`(){
        runBlocking {
            val favorites = flowOf(listOf(mockFavorite.copy(1)))
            whenever(favoriteRepository.getFavoriteList()).thenReturn(favorites)

            val result = myFavorite.getFavorites()

            Assert.assertEquals(favorites,result)
        }
    }

    @Test
    fun `remove favorite from repository`(){
        runBlocking {
            val event = mockEvent.copy("0123")

            myFavorite.remove(event)

            verify(favoriteRepository).deleteFavorite(event.id)
        }
    }

    @Test
    fun `save favorite in the repository`(){
        runBlocking {
            val event = mockEvent.copy("0123")
            val favorite = mockFavorite.copy(0,event.id)

            myFavorite.save(event)

            verify(favoriteRepository).saveNewFavorite(favorite)
        }
    }

}