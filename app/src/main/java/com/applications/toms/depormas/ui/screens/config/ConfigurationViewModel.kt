package com.applications.toms.depormas.ui.screens.config

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.applications.toms.depormas.utils.*
import kotlin.coroutines.coroutineContext

class ConfigurationViewModel: ViewModel(){

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode: LiveData<Boolean> get() = _darkMode

    init {

    }

    fun getDeviceDarkModeState(context: Context) {
        _darkMode.value = if (!getSharedPreferences(context).contains(SharedPreferencesKeys.DARK_MODE)) {
            when (AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_YES,
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> true
                else -> false
            }
        }else {
            getDarkMode(context)
        }
    }

    fun onDarkModeChecked(isChecked: Boolean){
        _darkMode.value = isChecked
    }

    fun updatedSelectedMode(context: Context){
        darkMode.value?.let {
            updateDarkMode(context, it)
            setSelectedMode(context)
        }
    }

}