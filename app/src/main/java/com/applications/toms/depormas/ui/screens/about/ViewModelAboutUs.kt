package com.applications.toms.depormas.ui.screens.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelAboutUs: ViewModel(){

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode: LiveData<Boolean> get() = _darkMode

    init {

    }

    fun getDarkMode(value: Boolean){
        _darkMode.value = value
    }
}