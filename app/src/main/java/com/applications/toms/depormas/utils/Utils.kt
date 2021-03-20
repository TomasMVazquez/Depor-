package com.applications.toms.depormas.utils

import android.app.Application
import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

fun setConstraintStatusBarMargin(context: Context, constraintLayout: ConstraintLayout) {
    val statusBarHeightId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    val statusBarHeight = context.resources.getDimensionPixelSize(statusBarHeightId)
    val layoutParams = ConstraintLayout.LayoutParams(constraintLayout.layoutParams)
    layoutParams.topMargin = statusBarHeight
    constraintLayout.layoutParams = layoutParams
}

fun setSelectedMode(context: Context){
    if (getSharedPreferences(context).contains(SharedPreferencesKeys.DARK_MODE)) {
        if (getDarkMode(context)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}