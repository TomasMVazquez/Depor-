package com.applications.toms.depormas.utils

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun Int.toPx(context: Context) = (this * context.resources.displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT

fun Context.getColorRes(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

fun <T> MutableLiveData<MutableList<T>>.addNewItem(item: T) {
    val oldValue = this.value ?: mutableListOf()
    oldValue.add(item)
    this.value = oldValue
}

fun <T> MutableLiveData<MutableList<T>>.addNewItemAt(index: Int, item: T) {
    val oldValue = this.value ?: mutableListOf()
    oldValue.add(index, item)
    this.value = oldValue
}

fun <T> MutableLiveData<MutableList<T>>.removeItemAt(index: Int) {
    if (!this.value.isNullOrEmpty()) {
        val oldValue = this.value
        oldValue?.removeAt(index)
        this.value = oldValue
    } else {
        this.value = mutableListOf()
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T: ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T {
    val vmFactory = object: ViewModelProvider.Factory{
        override fun <U : ViewModel?> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProvider(this,vmFactory).get(T::class.java)
}
