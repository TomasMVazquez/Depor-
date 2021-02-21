package com.applications.toms.depormas.utils

import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

fun Int.toPx(context: Context) = (this * context.resources.displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT

fun Context.getColorRes(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)


fun setConstraintStatusBarMargin(context: Context,constraintLayout: ConstraintLayout) {
    val statusBarHeightId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    val statusBarHeight = context.resources.getDimensionPixelSize(statusBarHeightId)
    val layoutParams = ConstraintLayout.LayoutParams(constraintLayout.layoutParams)
    layoutParams.topMargin = statusBarHeight
    constraintLayout.layoutParams = layoutParams
}