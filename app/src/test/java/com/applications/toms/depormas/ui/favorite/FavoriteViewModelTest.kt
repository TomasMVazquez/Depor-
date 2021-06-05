package com.applications.toms.depormas.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.applications.toms.depormas.CoroutinesTestRule
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.mockEvent
import com.applications.toms.depormas.mockFavorite
import com.applications.toms.depormas.ui.screens.favourite.FavoriteViewModel
import com.applications.toms.depormas.usecases.GetEvents
import com.applications.toms.depormas.usecases.MyFavorite
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoriteViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var myFavorite: MyFavorite

    @Mock
    lateinit var getEvents: GetEvents

    lateinit var viewModel: FavoriteViewModel

    private val events = listOf(mockEvent.copy(id = "01"),mockEvent.copy(id = "01"))
    private val favorites = listOf(mockFavorite.copy(id = 0,eventId = events[0].id))

    @Before
    fun setUp() {
        whenever(getEvents.invoke()).thenReturn(flowOf(events))
        whenever(myFavorite.getFavorites()).thenReturn(flowOf(favorites))

        viewModel = FavoriteViewModel(myFavorite,getEvents,Dispatchers.Unconfined)
    }

    @Test
    fun `validate getEvents is called`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val observer = mock<Observer<List<Event>>>()
            viewModel.events.asLiveData().observeForever(observer)

            verify(observer).onChanged(events)
        }
    }

    @Test
    fun `validate getFavorites is called`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val observer = mock<Observer<List<Event>>>()
            viewModel.favorites.observeForever(observer)

            verify(observer).onChanged(listOf(events[0]))
        }
    }

}