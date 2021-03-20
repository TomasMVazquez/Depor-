package com.applications.toms.depormas.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.applications.toms.depormas.data.model.Sport
import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.utils.Scope
import com.applications.toms.depormas.utils.Scope.ImplementJob
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel(private val sportRepository: SportRepository): ViewModel(), Scope by ImplementJob() {

    private val _sports = MutableLiveData<List<Sport>>()
    val sports: LiveData<List<Sport>> = _sports

    init {
        initScope()
        launch {
            _sports.value = sportRepository.getSports()
        }
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }

}