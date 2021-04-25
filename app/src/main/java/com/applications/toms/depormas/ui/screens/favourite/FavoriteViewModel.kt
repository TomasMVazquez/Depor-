package com.applications.toms.depormas.ui.screens.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.ui.screens.favourite.FavoriteViewModel.UiModel.*
import com.applications.toms.depormas.usecases.GetEvents
import com.applications.toms.depormas.usecases.GetFavorites
import com.applications.toms.depormas.utils.Scope
import kotlinx.coroutines.launch

class FavoriteViewModel(private val getFavorites: GetFavorites, private  val getEvents: GetEvents): ViewModel(), Scope by Scope.ImplementJob() {

    private val favorites = getFavorites.invoke()
    val events = getEvents.invoke().asLiveData()

    sealed class UiModel {
        object Loading: UiModel()
        class Content(val events: List<Event?>): UiModel()
    }

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) Loading
            return _model
        }

    private val _favoriteEvents = MutableLiveData<List<Event?>>()
    val favoriteEvents: LiveData<List<Event?>> get() = _favoriteEvents

    init {
        initScope()
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }

    fun getFavoriteEvents(list: List<Event>?) {
        launch {
            _model.value = Content(
                    favorites.map {
                        list?.find { event -> event.id == it.eventId }
                    }
            )
        }
    }


}