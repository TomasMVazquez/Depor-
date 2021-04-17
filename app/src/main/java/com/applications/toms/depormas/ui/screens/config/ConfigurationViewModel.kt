package com.applications.toms.depormas.ui.screens.config

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.applications.toms.depormas.preferences
import com.applications.toms.depormas.utils.*
import kotlin.coroutines.coroutineContext

class ConfigurationViewModel: ViewModel(){

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode: LiveData<Boolean> get() = _darkMode

    init {
        getDeviceDarkModeState()
    }

    private fun getDeviceDarkModeState(){
        _darkMode.value = if (preferences.isDarkModeSelected()) {
            when (AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_YES,
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> true
                else -> false
            }
        }else {
            preferences.darkMode
        }
    }

    fun onDarkModeChecked(isChecked: Boolean){
        _darkMode.value = isChecked
    }

    fun updatedSelectedMode(){
        darkMode.value?.let {
            preferences.darkMode = it
            setSelectedMode()
        }
    }

}