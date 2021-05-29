package com.applications.toms.depormas.viewmodel.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.applications.toms.depormas.domain.Location
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.mockSport
import com.applications.toms.depormas.ui.screens.create.CreateViewModel
import com.applications.toms.depormas.ui.screens.favourite.FavoriteViewModel
import com.applications.toms.depormas.usecases.GetSports
import com.applications.toms.depormas.usecases.SaveEvent
import com.applications.toms.depormas.utils.EventWrapper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class CreateViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getSports: GetSports

    @Mock
    lateinit var saveEvent: SaveEvent

    private lateinit var viewModel: CreateViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = CreateViewModel(getSports,saveEvent,Dispatchers.Unconfined)
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
        }
    }

    @Test
    fun `validate observer on cancel button is clicked`() {
        runBlocking {
            val observer = mock<Observer<EventWrapper<Boolean>>>()
            viewModel.cancel.observeForever(observer)

            viewModel.onCancelClicked()

            verify(observer).onChanged(viewModel.cancel.value)
            Assert.assertEquals(true,viewModel.cancel.value?.peekContent())
        }
    }

    @Test
    fun `validate observer on create button is clicked`() {
        runBlocking {
            val observer = mock<Observer<EventWrapper<Boolean>>>()
            viewModel.createEvent.observeForever(observer)

            viewModel.onCreateClicked()

            verify(observer).onChanged(viewModel.createEvent.value)
            Assert.assertEquals(true,viewModel.createEvent.value?.peekContent())
        }
    }

    @Test
    fun `validation of Event created`() {
        runBlocking {
            val observer = mock<Observer<EventWrapper<Int>>>()
            viewModel.createValidation.observeForever(observer)

            viewModel.onEventValidation(
                "",
                "",
                "",
                ""
            )

            verify(observer).onChanged(viewModel.createValidation.value)
            Assert.assertEquals(null,viewModel.createEvent.value?.peekContent())
        }
        /**
         * Como testear los livedata privados?
         */
    }
}