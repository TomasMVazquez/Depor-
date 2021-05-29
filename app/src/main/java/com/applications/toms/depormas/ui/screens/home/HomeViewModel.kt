package com.applications.toms.depormas.ui.screens.home

import androidx.lifecycle.*
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.domain.filterBySport
import com.applications.toms.depormas.domain.sportsAll
import com.applications.toms.depormas.usecases.*
import com.applications.toms.depormas.utils.EventWrapper
import com.applications.toms.depormas.utils.Scope
import com.applications.toms.depormas.utils.Scope.ImplementJob
import com.applications.toms.depormas.utils.ScopedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class HomeViewModel(
        private val getSports: GetSports,
        private val getEvents: GetEvents,
        private val isFavorites: IsFavorite,
        private val saveFavorite: SaveFavorite,
        uiDispatcher: CoroutineDispatcher
): ScopedViewModel(uiDispatcher) {

    private val _selectedSport = MutableStateFlow(sportsAll)
    val selectedSport: StateFlow<Sport> get() = _selectedSport

    private val _region = MutableStateFlow("")
    val region: StateFlow<String> get() = _region

    val sports = getSports.invoke()
        .flowOn(Dispatchers.IO)
        .catch { emit(emptyList()) }
        .asLiveData()

    val events = getEvents.invoke()
            .flowOn(Dispatchers.IO)
            .catch { emit(emptyList()) }
            .combine(region){ list, region -> list.filter { event ->
                if(region.isEmpty())
                    true
                else
                    event.location.countryCode == region
            } }
            .combine(selectedSport){ list, sport -> list.filterBySport(sport.id) }
            .conflate().asLiveData()

    private val _onFavoriteSaved = MutableLiveData<EventWrapper<Int>>()
    val onFavoriteSaved: LiveData<EventWrapper<Int>> get() = _onFavoriteSaved

    init {
        initScope()
    }

    fun updateRegion(region: String?) {
        _region.value = region ?: ""
    }

    fun onSelectSport(sportChecked: Sport?) {
        _selectedSport.value = sportChecked ?: sportsAll
    }

    fun onSwipeItemToAddToFavorite(event: Event?) {
        if (event != null) {
            if (isFavorites.invoke(event.id)){
                _onFavoriteSaved.value = EventWrapper(0)
            }else {
                event.addParticipant()
                saveFavorite.invoke(event)
                _onFavoriteSaved.value = EventWrapper(1)
            }
        }
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }

}