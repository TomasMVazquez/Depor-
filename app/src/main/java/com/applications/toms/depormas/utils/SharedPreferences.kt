package com.applications.toms.depormas.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

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
}