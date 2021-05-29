package com.applications.toms.depormas.viewmodel.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.mockEvent
import com.applications.toms.depormas.ui.screens.favourite.FavoriteViewModel
import com.applications.toms.depormas.usecases.GetEvents
import com.applications.toms.depormas.usecases.MyFavorite
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
class FavoriteViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var myFavorite: MyFavorite

    @Mock
    lateinit var getEvents: GetEvents

    lateinit var viewModel: FavoriteViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = FavoriteViewModel(myFavorite,getEvents,Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `validate getEvents is called`() {
        runBlocking {
            val events = listOf(mockEvent.copy(id = "01"))
            whenever(getEvents.invoke()).thenReturn(flowOf(events))

            val observer = mock<Observer<List<Event>>>()
            viewModel.events.asLiveData().observeForever(observer)

            verify(observer).onChanged(events)
            /**
             * Error "Argument(s) are different! Wanted:"
             */
        }
    }

    @Test
    fun `validate getEvents is called 2`() {
        runBlocking {
            val events = listOf(mockEvent.copy(id = "01"))
            whenever(getEvents.invoke()).thenReturn(flowOf(events))

            val list = viewModel.events.first()
            Assert.assertEquals(events,list)
            /**
             * Error:
                java.lang.AssertionError:
                Expected :[Event(id=01, event_name=Name, date=25/05/2022, time=18:00, sport=Sport(id=0, name=mockSport, img=, max_players=4, choosen=false), max_players=2, location=Location(id=-1, name=, address=, latitude=0.0, longitude=0.0, city=, countryCode=), created_date=Sat May 29 15:51:21 CEST 2021, participants=2)]
                Actual   :[]

             ----- Also tested with:
                viewModel.events.collect {
                    Assert.assertEquals(events,it)
                }

             ----And:
                val list = viewModel.events.collect()
                Assert.assertEquals(events,list)
             */
        }
    }

}