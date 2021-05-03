package com.applications.toms.depormas.data.database.local

import com.applications.toms.depormas.data.database.local.favorite.Favorite
import com.applications.toms.depormas.data.database.local.favorite.FavoriteDatabase
import com.applications.toms.depormas.data.source.favorite.LocalFavoriteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RoomFavoriteDataSource(db: FavoriteDatabase): LocalFavoriteDataSource {

    private val favoriteDao = db.favoriteDao

    override suspend fun isEmpty(): Boolean = withContext(Dispatchers.IO) { favoriteDao.favoriteCount() <= 0 }

    override fun saveFavorite(favorite: Favorite) = favoriteDao.insert(favorite)

    override fun findFavorite(eventId: String) = favoriteDao.findFavorite(eventId)

    override fun getAllFavorite(): Flow<List<Favorite>> = favoriteDao.getAll()

    override fun deleteFavorite(eventId: String) = favoriteDao.deleteFavorite(eventId)
}