package com.applications.toms.depormas.ui.screens.home

import androidx.lifecycle.*
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.domain.filterBySport
import com.applications.toms.depormas.usecases.GetEvents
import com.applications.toms.depormas.usecases.GetSports
import com.applications.toms.depormas.utils.Scope
import com.applications.toms.depormas.utils.Scope.ImplementJob
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel(
        private val getSports: GetSports,
        private val getEvents: GetEvents
        ): ViewModel(), Scope by ImplementJob() {

    val sports = getSports.invoke().asLiveData()
    val events = getEvents.invoke()

    private val allSports = Sport(-1,"","ic_all_sports",-1, false)

    private val _selectedSport = MutableLiveData<Sport>()
    val selectedSport: LiveData<Sport> get() = _selectedSport

    sealed class UiModel {
        object Loading: UiModel()
        class Content(val events: List<Event>): UiModel()
        object RequestLocationPermission : UiModel()
    }

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    init {
        initScope()
        _selectedSport.value = allSports
    }

    private fun refresh(){
        _model.value = UiModel.RequestLocationPermission
    }

    fun onCoarseLocationPermissionRequested() {
        launch {
            _model.value = UiModel.Loading
            _model.value = UiModel.Content(events)
        }
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }

    fun onSelectSport(sportChecked: Sport?) {
        _selectedSport.value = sportChecked ?: allSports
    }

    fun onFilterEventsBySportSelected(sport: Sport){
        launch {
            _model.value = UiModel.Loading
            val filteredEvents = if(sport.id!! < 0) events else events.filterBySport(sport)
            _model.value = UiModel.Content(filteredEvents)
        }
    }

}