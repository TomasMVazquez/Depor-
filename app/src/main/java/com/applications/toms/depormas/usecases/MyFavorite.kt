package com.applications.toms.depormas.usecases

import com.applications.toms.depormas.data.database.local.favorite.Favorite
import com.applications.toms.depormas.data.repository.FavoriteRepository

class MyFavorite(private val favoriteRepository: FavoriteRepository) {
    fun getFavorites() = favoriteRepository.getFavoriteList()
    fun remove(eventId: String) = favoriteRepository.deleteFavorite(eventId)
    fun save(favorite: Favorite) = favoriteRepository.saveNewFavorite(favorite)
}