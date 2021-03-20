@file:Suppress("UNCHECKED_CAST")

package com.applications.toms.depormas.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applications.toms.depormas.data.repository.SportRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeViewModelFactory (private val sportRepository: SportRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(sportRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}