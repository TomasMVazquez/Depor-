package com.applications.toms.depormas.ui.screens.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.domain.Location
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.ui.screens.home.HomeViewModel
import com.applications.toms.depormas.usecases.GetSports
import com.applications.toms.depormas.usecases.SaveEvent
import com.applications.toms.depormas.utils.EventWrapper
import com.applications.toms.depormas.utils.Scope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreateViewModel(private val getSports: GetSports, private val saveEvent: SaveEvent): ViewModel(), Scope by Scope.ImplementJob(){

    private val _sports= MutableLiveData<List<Sport>>()
    val sports: LiveData<List<Sport>> get() = _sports


    private val _eventLocation = MutableLiveData<Location>()
    private val _eventSport = MutableLiveData<Sport>()

    private val _cancel = MutableLiveData<EventWrapper<Boolean>>()
    val cancel: LiveData<EventWrapper<Boolean>> get() = _cancel

    private val _createEvent = MutableLiveData<EventWrapper<Boolean>>()
    val createEvent: LiveData<EventWrapper<Boolean>> get() = _createEvent

    private val _createValidation = MutableLiveData<EventWrapper<Int>>()
    val createValidation: LiveData<EventWrapper<Int>> get() = _createValidation

    init {
        initScope()
        launch {
            getSports.invoke().collect{
                _sports.value = it
            }
        }
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }

    fun onCancelClicked(){
        _cancel.value = EventWrapper(true)
    }

    fun onCreateClicked(){
        _createEvent.value = EventWrapper(true)
    }

    fun setEventLocation(value: Location){
        _eventLocation.value = value
    }

    fun setEventSport(value: Sport){
        _eventSport.value = value
    }

    fun onEventValidation(name: String?, day: String?, time: String?, participants: String?) {
        // TODO ENUM????
        _createValidation.value = when{
            _eventSport.value == null -> EventWrapper(0)
            name.isNullOrEmpty() -> EventWrapper(1)
            day.isNullOrEmpty() -> EventWrapper(2)
            time.isNullOrEmpty() -> EventWrapper(3)
            _eventLocation.value == null -> EventWrapper(4)
            else -> {
                onCreateNetworkEvent(name,day,time,participants)
                EventWrapper(-1)
            }
        }
    }

    private fun onCreateNetworkEvent(name: String, day: String, time: String, participants: String?) {
        launch {
            saveEvent.invoke(
                    Event(
                            id = "",
                            event_name = name,
                            date = day,
                            time = time,
                            sport = _eventSport.value!!,
                            location = _eventLocation.value!!,
                            max_players = if (!participants.isNullOrBlank()) participants.toInt() else -1,
                    )
            )
        }
    }
}