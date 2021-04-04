package com.applications.toms.depormas.ui.screens.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.applications.toms.depormas.data.model.Sport
import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.utils.EventWrapper
import com.applications.toms.depormas.utils.Scope

class CreateViewModel(private val sportRepository: SportRepository): ViewModel(), Scope by Scope.ImplementJob(){

    val sports = sportRepository.getSports().asLiveData()

    private val _cancel = MutableLiveData<EventWrapper<Boolean>>()
    val cancel: LiveData<EventWrapper<Boolean>> get() = _cancel

    private val _createEvent = MutableLiveData<EventWrapper<Boolean>>()
    val createEvent: LiveData<EventWrapper<Boolean>> get() = _createEvent

    init {
        initScope()
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
}