package com.applications.toms.depormas.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

fun getSharedPreferences(application: Application): SharedPreferences{
    return application.getSharedPreferences("preferences", Context.MODE_PRIVATE)
}

object SharedPreferencesKeys{
    const val DARK_MODE = "dark_mode"
}

fun updateDarkMode(application: Application, value: Boolean){
    getSharedPreferences(application).edit { putBoolean(SharedPreferencesKeys.DARK_MODE,value) }
}

fun getDarkMode(application: Application): Boolean{
    return getSharedPreferences(application).getBoolean(SharedPreferencesKeys.DARK_MODE,false)
}