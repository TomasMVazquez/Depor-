package com.applications.toms.depormas.datatest.repositorytest

import com.applications.toms.depormas.data.repository.FavoriteRepository
import com.applications.toms.depormas.data.source.favorite.LocalFavoriteDataSource
import com.applications.toms.depormas.mockFavorite
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
class FavoriteRepositoryTest {

    @Mock
    lateinit var localFavoriteDataSource: LocalFavoriteDataSource

    lateinit var favoriteRepository: FavoriteRepository

    @Before
    fun setUp(){
        favoriteRepository = FavoriteRepository(localFavoriteDataSource)
    }

    @Test
    fun `getFavorites get list from local`(){
        runBlocking {
            val favoriteList = flowOf(listOf(mockFavorite.copy(id = 5)))
            whenever(localFavoriteDataSource.getAllFavorite()).thenReturn(favoriteList)

            val result = favoriteRepository.getFavoriteList()

            Assert.assertEquals(favoriteList,result)
        }
    }

    @Test
    fun `saveFavorite in local`(){
        runBlocking {
            val favorite = mockFavorite.copy(id = 8)

            favoriteRepository.saveNewFavorite(favorite)

            verify(localFavoriteDataSource).saveFavorite(favorite)
        }
    }

    @Test
    fun `deleteFavorite from local`(){
        runBlocking {
            val favorite = mockFavorite.copy(eventId = "event")

            favoriteRepository.deleteFavorite(favorite.eventId)

            verify(localFavoriteDataSource).deleteFavorite(favorite.eventId)
        }
    }

    @Test
    fun `isFavorite return true`(){
        runBlocking {
            val favorite = mockFavorite.copy(eventId = "event2")
            whenever(localFavoriteDataSource.findFavorite(favorite.eventId)).thenReturn(1)

            val result = favoriteRepository.isFavorite(favorite.eventId)

            Assert.assertTrue(result)
        }
    }

    @Test
    fun `isFavorite return false`(){
        runBlocking {
            val favorite = mockFavorite.copy(eventId = "event2")
            whenever(localFavoriteDataSource.findFavorite(favorite.eventId)).thenReturn(0)

            val result = favoriteRepository.isFavorite(favorite.eventId)

            Assert.assertFalse(result)
        }
    }
}