package com.applications.toms.depormas.data.database.local

import com.applications.toms.depormas.data.database.local.favorite.Favorite
import com.applications.toms.depormas.data.database.local.favorite.FavoriteDatabase
import com.applications.toms.depormas.data.source.favorite.LocalFavoriteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomFavoriteDataSource(db: FavoriteDatabase): LocalFavoriteDataSource {

    private val favoriteDao = db.favoriteDao

    override suspend fun isEmpty(): Boolean = withContext(Dispatchers.IO) { favoriteDao.favoriteCount() <= 0 }

    override suspend fun saveFavorite(favorite: Favorite) = withContext(Dispatchers.IO) { favoriteDao.insert(favorite) }

    override fun getAllFavorite(): List<Favorite> = favoriteDao.getAll()

    override suspend fun deleteFavorite(favorite: Favorite) = withContext(Dispatchers.IO){ favoriteDao.deleteFavorite(favorite) }
}