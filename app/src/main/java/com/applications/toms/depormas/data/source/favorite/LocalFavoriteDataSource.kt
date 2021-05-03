package com.applications.toms.depormas.data.source.favorite

import com.applications.toms.depormas.data.database.local.favorite.Favorite
import kotlinx.coroutines.flow.Flow

interface LocalFavoriteDataSource {
    suspend fun isEmpty():Boolean
    fun saveFavorite(favorite: Favorite)
    fun findFavorite(eventId: String): Int
    fun getAllFavorite(): Flow<List<Favorite>>
    fun deleteFavorite(eventId: String)
}