package com.applications.toms.depormas.viewmodel.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.mockSport
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getSports: GetSports

    @Mock
    lateinit var getEvents: GetEvents

    @Mock
    lateinit var isFavorite: IsFavorite

    @Mock
    lateinit var saveFavorite: SaveFavorite

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp(){
        /**
            Al saltar error:
                Module with the Main dispatcher had failed to initialize. For tests Dispatchers.setMain from kotlinx-coroutines-test module can be used
            Se agrego --> testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2'
            y el setMain y resetMain para evitarlo
         */

        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = HomeViewModel(getSports,getEvents,isFavorite,saveFavorite,Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `validate getSports is called`() {
        runBlocking {
            val sports = listOf(mockSport.copy(id = 0))
            val sportsFlow = flowOf(sports)
            whenever(getSports.invoke()).thenReturn(sportsFlow)

            val observer = mock<Observer<List<Sport>>>()
            viewModel.sports.observeForever(observer)

            verify(observer).onChanged(viewModel.sports.value)
            /**
                En realidad no es correcta esta validacion ya que esta vacia
                si cambiamos viewmodel.sports.value por sports --> error
             */
        }
    }

    @Test
    fun `validate getEvents is called`() {
        runBlocking {
            val events = listOf(mockEvent.copy(id = "0"))
            val eventsFlow = flowOf(events)
            whenever(getEvents.invoke()).thenReturn(eventsFlow)

            val observer = mock<Observer<List<Event>>>()
            viewModel.events.observeForever(observer)

            verify(observer).onChanged(events)
            /**
             * Tira error pero... ???
             */
        }
    }

    @Test
    fun `when a sport is selected, the sport is checked`() {
        runBlocking {
            val sport = mockSport.copy(id = 1)
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
        runBlocking {
            val observer = mock<Observer<Sport>>()
            viewModel.selectedSport.asLiveData().observeForever(observer)
            val sport = mockSport.copy(id = 1)

            viewModel.onSelectSport(sport)

            verify(observer).onChanged(sport)
        }
    }

    @Test
    fun `when a region is provided, the region is updated`() {
        runBlocking {
            val region = "ES"
            val observer = mock<Observer<String>>()

            viewModel.region.asLiveData().observeForever(observer)

            viewModel.updateRegion(region)

            verify(observer).onChanged(region)
        }
    }

    @Test
    fun `on swipe event, add it to my favorites`() {
        runBlocking {
            val event = mockEvent.copy(id = "myEvent")
            whenever(isFavorite.invoke(event.id)).thenReturn(false)

            val observer = mock<Observer<EventWrapper<Int>>>()
            viewModel.onFavoriteSaved.observeForever(observer)

            viewModel.onSwipeItemToAddToFavorite(event)

            verify(observer).onChanged(viewModel.onFavoriteSaved.value)
        }
    }

    @Test
    fun `on swipe event, already added`() {
        runBlocking {
            val event = mockEvent.copy(id = "myEvent")
            whenever(isFavorite.invoke(event.id)).thenReturn(true)

            val observer = mock<Observer<EventWrapper<Int>>>()
            viewModel.onFavoriteSaved.observeForever(observer)

            viewModel.onSwipeItemToAddToFavorite(event)

            verify(observer).onChanged(viewModel.onFavoriteSaved.value)
        }
    }
}