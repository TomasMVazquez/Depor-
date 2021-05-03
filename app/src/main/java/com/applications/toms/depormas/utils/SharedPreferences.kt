package com.applications.toms.depormas.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class SharedPreferences(context: Context){
    private val preferences: SharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    var darkMode: Boolean
        get() = preferences.getBoolean(DARK_MODE,false)
        set(value) = preferences.edit { putBoolean(DARK_MODE,value) }

    var onBoarding: Boolean
        get() = preferences.getBoolean(ON_BOARDING,false)
        set(value) = preferences.edit { putBoolean(ON_BOARDING,value) }

    var permissionToLocation: Boolean
        get() = preferences.getBoolean(LOCATION_PERMISSION,false)
        set(value) = preferences.edit(){ putBoolean(LOCATION_PERMISSION,value) }

    fun isDarkModeSelected(): Boolean = preferences.contains(DARK_MODE)

    companion object{
        private const val DARK_MODE = "dark_mode"
        private const val ON_BOARDING = "onBoarding"
        private const val LOCATION_PERMISSION = "location_permission"
    }
}
/*

fun getSharedPreferences(context: Context): SharedPreferences{
    return context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
}

object SharedPreferencesKeys{
    const val DARK_MODE = "dark_mode"
    const val ON_BOARDING = "onBoarding"
}

fun updateDarkMode(context: Context, value: Boolean){
    getSharedPreferences(context).edit { putBoolean(SharedPreferencesKeys.DARK_MODE,value) }
}

fun getDarkMode(context: Context): Boolean{
    return getSharedPreferences(context).getBoolean(SharedPreferencesKeys.DARK_MODE,false)
}

fun onFinishOnBoarding(context: Context){
    getSharedPreferences(context).edit{ putBoolean(SharedPreferencesKeys.ON_BOARDING,true) }
}

fun onBoardingFinished(context: Context): Boolean{
    return getSharedPreferences(context).getBoolean(SharedPreferencesKeys.ON_BOARDING,false)
}*/
