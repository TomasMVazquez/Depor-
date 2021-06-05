package com.applications.toms.depormas.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.applications.toms.depormas.*
import com.applications.toms.depormas.data.source.favorite.LocalFavoriteDataSource
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.ui.screens.favourite.FavoriteViewModel
import com.applications.toms.depormas.usecases.*
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoriteIntegrationTest: KoinTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var getEvents: GetEvents

    private lateinit var viewModel: FavoriteViewModel

    @Before
    fun setUp() {
        val vmModule = module {
            factory { FavoriteViewModel(get(),get(),get()) }
            factory { MyFavorite(get(),get()) }
            factory { GetEvents(get()) }
        }

        initMockDi(vmModule)
        viewModel = get()
    }

    @After
    fun tearDown(){
        stopKoin()
    }

    @Test
    fun `data of Favorites is loaded from local when available`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            whenever(getEvents.invoke()).thenReturn(flowOf(defaultFakeEvents))
        }
        val localDataSource = get<LocalFavoriteDataSource>() as FakeRoomFavoriteDataSource
        localDataSource.favorites = defaultFakeFavorite

        val resultExpected = defaultFakeEvents.subList(0,1)

        val observer = mock<Observer<List<Event>>>()
        viewModel.favorites.observeForever(observer)

        verify(observer).onChanged(resultExpected)
    }

}