package com.applications.toms.depormas.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun Int.toPx(context: Context) = (this * context.resources.displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT

fun View.snackBar(message: String, duration: Int = BaseTransientBottomBar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T: ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T {
    val vmFactory = object: ViewModelProvider.Factory{
        override fun <U : ViewModel?> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProvider(this,vmFactory).get(T::class.java)
}
