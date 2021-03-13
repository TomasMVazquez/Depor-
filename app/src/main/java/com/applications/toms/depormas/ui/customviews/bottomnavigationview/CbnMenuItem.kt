package com.applications.toms.depormas.ui.customviews.bottomnavigationview

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import com.applications.toms.depormas.R

data class CbnMenuItem(
    @DrawableRes
    val icon: Int,
    @DrawableRes
    val avdIcon: Int,
    @IdRes
    val destinationId: Int = -1
)
