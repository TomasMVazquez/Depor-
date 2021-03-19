package com.applications.toms.depormas.ui.screens.about

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.applications.toms.depormas.utils.getDarkMode

class ViewModelAboutUs(application: Application): AndroidViewModel(application){

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode: LiveData<Boolean> get() = _darkMode

    init {
        _darkMode.value = getDarkMode(getApplication())
    }
}