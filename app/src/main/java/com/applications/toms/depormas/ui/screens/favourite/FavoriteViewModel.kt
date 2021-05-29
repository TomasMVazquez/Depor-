package com.applications.toms.depormas.ui.screens.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.usecases.GetEvents
import com.applications.toms.depormas.usecases.MyFavorite
import com.applications.toms.depormas.utils.EventWrapper
import com.applications.toms.depormas.utils.Scope
import com.applications.toms.depormas.utils.ScopedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class FavoriteViewModel(
        private val myFavorite: MyFavorite,
        private  val getEvents: GetEvents,
        override val uiDispatcher: CoroutineDispatcher
): ScopedViewModel(uiDispatcher) {

    val events = getEvents.invoke()
        .flowOn(Dispatchers.IO)
        .catch { emit(emptyList()) }

    val favorites = myFavorite.getFavorites()
            .flowOn(Dispatchers.IO).catch { emit(emptyList()) }
            .combine(events){list, events ->
                list.map { favorite ->
                    events.find { favorite.eventId == it.id }!!
                }
            }
            .conflate().asLiveData()

    private val _onFavoriteRemoved = MutableLiveData<EventWrapper<Map<String,Any>>>()
    val onFavoriteRemoved: LiveData<EventWrapper<Map<String,Any>>> get() = _onFavoriteRemoved

    init {
        initScope()
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }

    fun onSwipeItemToRemoveFavorite(event: Event?) {
        if (event != null){
            launch {
                event.removeParticipant()
                myFavorite.remove(event)
                _onFavoriteRemoved.value = EventWrapper(mapOf(
                        FavouriteFragment.STATUS to 0,
                        FavouriteFragment.FAVORITE to event
                ))
            }
        }
    }

    fun saveFavoriteAfterRemoveIt(event: Event) {
        launch {
            event.addParticipant()
            myFavorite.save(event)
            _onFavoriteRemoved.value = EventWrapper(mapOf(
                    FavouriteFragment.STATUS to 1,
                    FavouriteFragment.FAVORITE to event.id
            ))
        }
    }

}