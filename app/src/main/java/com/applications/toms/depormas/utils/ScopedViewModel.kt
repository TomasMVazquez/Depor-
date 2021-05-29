package com.applications.toms.depormas.utils

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher

abstract class ScopedViewModel(uiDispatcher: CoroutineDispatcher) : ViewModel(),
    Scope by Scope.ImplementJob(uiDispatcher) {

    init {
        initScope()
    }

    @CallSuper
    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }
}