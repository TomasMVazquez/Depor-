package com.applications.toms.depormas.ui.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.applications.toms.depormas.CoroutinesTestRule
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.mockSport
import com.applications.toms.depormas.ui.screens.create.CreateViewModel
import com.applications.toms.depormas.usecases.GetSports
import com.applications.toms.depormas.usecases.SaveEvent
import com.applications.toms.depormas.utils.EventWrapper
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
class CreateViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var getSports: GetSports

    @Mock
    lateinit var saveEvent: SaveEvent

    private lateinit var viewModel: CreateViewModel

    private val sports = listOf(mockSport.copy(id = 0),mockSport.copy(id = 1),mockSport.copy(id = 2))

    @Before
    fun setUp() {
        whenever(getSports.invoke()).thenReturn(flowOf(sports))

        viewModel = CreateViewModel(getSports,saveEvent,Dispatchers.Unconfined)
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
    fun `validate observer on cancel button is clicked`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val observer = mock<Observer<EventWrapper<Boolean>>>()
            viewModel.cancel.observeForever(observer)

            viewModel.onCancelClicked()

            verify(observer).onChanged(viewModel.cancel.value)
            Assert.assertEquals(true,viewModel.cancel.value?.peekContent())
        }
    }

    @Test
    fun `validate observer on create button is clicked`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val observer = mock<Observer<EventWrapper<Boolean>>>()
            viewModel.createEvent.observeForever(observer)

            viewModel.onCreateClicked()

            verify(observer).onChanged(viewModel.createEvent.value)
            Assert.assertEquals(true,viewModel.createEvent.value?.peekContent())
        }
    }

    @Test
    fun `validation of Event created`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
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
    }
}