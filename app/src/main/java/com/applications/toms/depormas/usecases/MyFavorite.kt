package com.applications.toms.depormas.usecases

import com.applications.toms.depormas.data.database.local.favorite.Favorite
import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.data.repository.FavoriteRepository
import com.applications.toms.depormas.domain.Event

class MyFavorite(private val favoriteRepository: FavoriteRepository,private val eventRepository: EventRepository) {
    fun getFavorites() = favoriteRepository.getFavoriteList()
    fun remove(event: Event) {
        eventRepository.updateEvent(event)
        favoriteRepository.deleteFavorite(event.id)
    }
    fun save(event: Event) {
        eventRepository.updateEvent(event)
        favoriteRepository.saveNewFavorite(Favorite(0, event.id))
    }
}