package com.applications.toms.depormas.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.applications.toms.depormas.data.model.Sport
import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.utils.Scope
import com.applications.toms.depormas.utils.Scope.ImplementJob
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeViewModel(private val sportRepository: SportRepository): ViewModel(), Scope by ImplementJob() {

    val sports = sportRepository.getSports().asLiveData()

    private val allSports = Sport(-1,"","ic_all_sports",-1, false)

    private val _selectedSport = MutableLiveData<Sport>()
    val selectedSport: LiveData<Sport> get() = _selectedSport

    init {
        initScope()
        _selectedSport.value = allSports
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }

    fun onSelectSport(sportChecked: Sport?) {
        _selectedSport.value = sportChecked ?: allSports
    }

}