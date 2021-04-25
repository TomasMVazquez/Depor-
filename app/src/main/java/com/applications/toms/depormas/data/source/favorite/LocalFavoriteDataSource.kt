package com.applications.toms.depormas.data.source.favorite

import com.applications.toms.depormas.data.database.local.favorite.Favorite

interface LocalFavoriteDataSource {
    suspend fun isEmpty():Boolean
    suspend fun saveFavorite(favorite: Favorite)
    fun getAllFavorite(): List<Favorite>
    suspend fun deleteFavorite(favorite: Favorite)
}