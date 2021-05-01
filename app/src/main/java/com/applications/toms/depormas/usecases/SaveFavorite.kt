package com.applications.toms.depormas.usecases

import com.applications.toms.depormas.data.database.local.favorite.Favorite
import com.applications.toms.depormas.data.repository.FavoriteRepository

class SaveFavorite(private val favoriteRepository: FavoriteRepository) {
    suspend fun invoke(favorite: Favorite) = favoriteRepository.saveNewFavorite(favorite)
}