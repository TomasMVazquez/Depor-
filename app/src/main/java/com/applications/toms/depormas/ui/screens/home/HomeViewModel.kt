package com.applications.toms.depormas.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.applications.toms.depormas.data.model.Sport
import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.utils.Scope
import com.applications.toms.depormas.utils.Scope.ImplementJob
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeViewModel(private val sportRepository: SportRepository): ViewModel(), Scope by ImplementJob() {

    val sports: LiveData<List<Sport>> = sportRepository.getSports().asLiveData()

    init {
        initScope()
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }

}