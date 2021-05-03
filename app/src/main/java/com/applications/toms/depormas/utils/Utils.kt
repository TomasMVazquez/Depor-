package com.applications.toms.depormas.utils

import android.app.Application
import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.preferences
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun setConstraintStatusBarMargin(context: Context, constraintLayout: ConstraintLayout) {
    val statusBarHeightId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    val statusBarHeight = context.resources.getDimensionPixelSize(statusBarHeightId)
    val layoutParams = ConstraintLayout.LayoutParams(constraintLayout.layoutParams)
    layoutParams.topMargin = statusBarHeight
    constraintLayout.layoutParams = layoutParams
}

fun setSelectedMode(){
    if (preferences.isDarkModeSelected()){
        if (preferences.darkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}

val dateStringComparator = Comparator { event1: Event, event2: Event ->
    val df = DateTimeFormatter.ofPattern("d/MM/yyyy")
    val date1 = LocalDate.parse(event1.date,df)
    val date2 = LocalDate.parse(event2.date, df)
    date1.compareTo(date2)
}