package com.applications.toms.depormas.usecasestest

import com.applications.toms.depormas.data.repository.FavoriteRepository
import com.applications.toms.depormas.usecases.IsFavorite
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.nhaarman.mockitokotlin2.whenever

@RunWith(MockitoJUnitRunner::class)
class IsFavoriteTest {

    @Mock
    lateinit var favoriteRepository: FavoriteRepository

    lateinit var isFavorite: IsFavorite

    @Before
    fun setUp(){
        isFavorite = IsFavorite(favoriteRepository)
    }

    @Test
    fun `event is favorite`(){
        runBlocking {
            whenever(favoriteRepository.isFavorite("1")).thenReturn(true)

            val result = isFavorite.invoke("1")

            Assert.assertTrue(result)
        }
    }

    @Test
    fun `event is not favorite`(){
        runBlocking {
            whenever(favoriteRepository.isFavorite("1")).thenReturn(false)

            val result = isFavorite.invoke("1")

            Assert.assertFalse(result)
        }
    }

}