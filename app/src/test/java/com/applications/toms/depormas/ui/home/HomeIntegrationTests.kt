package com.applications.toms.depormas.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.applications.toms.depormas.*
import com.applications.toms.depormas.data.source.events.LocalEventDataSource
import com.applications.toms.depormas.data.source.favorite.LocalFavoriteDataSource
import com.applications.toms.depormas.data.source.sports.LocalSportDataSource
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.mockSport
import com.applications.toms.depormas.ui.screens.home.HomeViewModel
import com.applications.toms.depormas.usecases.GetEvents
import com.applications.toms.depormas.usecases.GetSports
import com.applications.toms.depormas.usecases.IsFavorite
import com.applications.toms.depormas.usecases.SaveFavorite
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeIntegrationTests: KoinTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        val vmModule = module {
            factory { HomeViewModel(get(),get(),get(),get(),get()) }
            factory { GetSports(get()) }
            factory { GetEvents(get()) }
            factory { IsFavorite(get()) }
            factory { SaveFavorite(get(),get()) }
        }

        initMockDi(vmModule)
        viewModel = get()
    }

    @After
    fun tearDown(){
        stopKoin()
    }

    @Test
    fun `data of Sports is loaded from server when local source is empty`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val observer = mock<Observer<List<Sport>>>()
            viewModel.sports.observeForever(observer)

            verify(observer).onChanged(defaultFakeSports)
        }
    }

    @Test
    fun `data of Sports is loaded from local when available`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val fakeLocalSports = listOf(mockSport.copy(id = 2),mockSport.copy(id = 3))
            val localDataSource = get<LocalSportDataSource>() as FakeRoomSportDataSource
            localDataSource.sports = fakeLocalSports

            val observer = mock<Observer<List<Sport>>>()
            viewModel.sports.observeForever(observer)

            verify(observer).onChanged(viewModel.sports.value)
        }
    }

    @Test
    fun `data of Events is loaded from server when local source is empty`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            val observer = mock<Observer<List<Event>>>()
            viewModel.events.observeForever(observer)

            verify(observer).onChanged(defaultFakeEvents)
        }
    }

    @Test
    fun `data of Events is loaded from local when available`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val fakeLocalEvents = listOf(mockEvent.copy(id = "2"),mockEvent.copy(id = "3"))
            val localDataSource = get<LocalEventDataSource>() as FakeRoomEventDataSource
            localDataSource.events = fakeLocalEvents

            val observer = mock<Observer<List<Event>>>()
            viewModel.events.observeForever(observer)

            verify(observer).onChanged(viewModel.events.value)
        }
    }

    @Test
    fun `favorite saved on local`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val localDataSource = get<LocalFavoriteDataSource>() as FakeRoomFavoriteDataSource

            viewModel.onSwipeItemToAddToFavorite(defaultFakeEvents[0])

            val favorites = listOf(mockFavorite.copy(id = 0,eventId = defaultFakeEvents[0].id))

            Assert.assertEquals(localDataSource.favorites, favorites)
        }
    }

}