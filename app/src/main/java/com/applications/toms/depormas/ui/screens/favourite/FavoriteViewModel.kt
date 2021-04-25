package com.applications.toms.depormas.ui.screens.favourite

import android.util.Log
import androidx.lifecycle.ViewModel
import com.applications.toms.depormas.usecases.GetFavorites
import com.applications.toms.depormas.utils.Scope

class FavoriteViewModel(private val getFavorites: GetFavorites): ViewModel(), Scope by Scope.ImplementJob() {

    private val favorites = getFavorites.invoke()

    init {
        initScope()
        Log.d("FAVORITE", "total = $favorites ")
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }


}