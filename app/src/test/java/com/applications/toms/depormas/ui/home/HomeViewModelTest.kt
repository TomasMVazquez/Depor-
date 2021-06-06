package com.applications.toms.depormas.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.mockSport
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.applications.toms.depormas.CoroutinesTestRule
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.mockEvent
import com.applications.toms.depormas.ui.screens.home.HomeViewModel
import com.applications.toms.depormas.usecases.GetEvents
import com.applications.toms.depormas.usecases.GetSports
import com.applications.toms.depormas.usecases.IsFavorite
import com.applications.toms.depormas.usecases.SaveFavorite
import com.applications.toms.depormas.utils.EventWrapper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var getSports: GetSports

    @Mock
    lateinit var getEvents: GetEvents

    @Mock
    lateinit var isFavorite: IsFavorite

    @Mock
    lateinit var saveFavorite: SaveFavorite

    private lateinit var viewModel: HomeViewModel

    private val sports = listOf(mockSport.copy(id = 0),mockSport.copy(id = 1),mockSport.copy(id = 2))
    private val events = listOf(mockEvent.copy(id = "01"),mockEvent.copy(id = "01"))

    @Before
    fun setUp(){
        coroutinesTestRule.testDispatcher.runBlockingTest {
            whenever(getSports.invoke()).thenReturn(flowOf(sports))
        }
        whenever(getEvents.invoke()).thenReturn(flowOf(events))

        viewModel = HomeViewModel(getSports,getEvents,isFavorite,saveFavorite,Dispatchers.Unconfined)
    }

    @Test
    fun `validate getSports is called`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val observer = mock<Observer<List<Sport>>>()
            viewModel.sports.observeForever(observer)

            verify(observer).onChanged(sports)
        }
    }

    @Test
    fun `validate getEvents is called`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val observer = mock<Observer<List<Event>>>()
            viewModel.events.observeForever(observer)

            verify(observer).onChanged(events)
        }
    }

    @Test
    fun `when a sport is selected, the sport is checked`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val sport = sports[0]
            var selectedSport = viewModel.selectedSport.first()

            Assert.assertNotEquals(selectedSport,sport)

            viewModel.onSelectSport(sport)
            selectedSport = viewModel.selectedSport.first()

            Assert.assertEquals(selectedSport,sport)
        }
    }

    /**
     * Another way to test stateFlow
     */

    @Test
    fun `when a sport is selected, the sport is checked 2`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val observer = mock<Observer<Sport>>()
            viewModel.selectedSport.asLiveData().observeForever(observer)
            val sport = sports[0]

            viewModel.onSelectSport(sport)

            verify(observer).onChanged(sport)
        }
    }

    @Test
    fun `when a region is provided, the region is updated`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val region = "ES"
            val observer = mock<Observer<String>>()

            viewModel.region.asLiveData().observeForever(observer)

            viewModel.updateRegion(region)

            verify(observer).onChanged(region)
        }
    }

    @Test
    fun `on swipe event, add it to my favorites`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val event = events[0]
            whenever(isFavorite.invoke(event.id)).thenReturn(false)

            val observer = mock<Observer<EventWrapper<Int>>>()
            viewModel.onFavoriteSaved.observeForever(observer)

            viewModel.onSwipeItemToAddToFavorite(event)

            verify(observer).onChanged(viewModel.onFavoriteSaved.value)
        }
    }

    @Test
    fun `on swipe event, already added`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val event = events[0]
            whenever(isFavorite.invoke(event.id)).thenReturn(true)

            val observer = mock<Observer<EventWrapper<Int>>>()
            viewModel.onFavoriteSaved.observeForever(observer)

            viewModel.onSwipeItemToAddToFavorite(event)

            verify(observer).onChanged(viewModel.onFavoriteSaved.value)
        }
    }
}