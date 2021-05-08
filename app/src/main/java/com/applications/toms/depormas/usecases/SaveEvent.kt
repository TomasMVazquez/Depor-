package com.applications.toms.depormas.usecases

import com.applications.toms.depormas.data.database.local.favorite.Favorite
import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.data.repository.FavoriteRepository
import com.applications.toms.depormas.domain.Event

class SaveEvent(private val eventRepository: EventRepository, private val favoriteRepository: FavoriteRepository) {
    fun invoke(event: Event){
        val idSaved = eventRepository.saveEvent(event)
        favoriteRepository.saveNewFavorite(Favorite(0,idSaved))
    }
}