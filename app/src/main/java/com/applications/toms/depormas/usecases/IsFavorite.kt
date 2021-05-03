package com.applications.toms.depormas.usecases

import com.applications.toms.depormas.data.repository.FavoriteRepository

class IsFavorite(private val favoriteRepository: FavoriteRepository) {
    fun invoke(eventId: String): Boolean = favoriteRepository.isFavorite(eventId)
}