package com.applications.toms.depormas.ui.screens.config

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.applications.toms.depormas.utils.*

class ConfigurationViewModel(application: Application): AndroidViewModel(application){

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode: LiveData<Boolean> get() = _darkMode

    init {
        _darkMode.value = getDeviceDarkModeState()
    }

    private fun getDeviceDarkModeState(): Boolean{
        return if (!getSharedPreferences(getApplication()).contains(SharedPreferencesKeys.DARK_MODE)) {
            when (AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_YES,
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> true
                else -> false
            }
        }else {
            getDarkMode(getApplication())
        }
    }

    fun onDarkModeChecked(isChecked: Boolean){
        _darkMode.value = isChecked
    }

    fun updatedSelectedMode(){
        darkMode.value?.let {
            updateDarkMode(getApplication(), it)
            setSelectedMode(getApplication())
        }
    }

}