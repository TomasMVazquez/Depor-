package com.applications.toms.depormas.usecases

import com.applications.toms.depormas.data.database.local.favorite.Favorite
import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.data.repository.FavoriteRepository
import com.applications.toms.depormas.domain.Event

class SaveEvent(private val eventRepository: EventRepository, private val favoriteRepository: FavoriteRepository) {
    suspend fun invoke(event: Event){
        eventRepository.saveEvent(event)
        favoriteRepository.saveNewFavorite(Favorite(0,event.id)) //TODO -> ID "" porque aun no esta en firestore
    }
}