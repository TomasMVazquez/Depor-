package com.applications.toms.depormas.data.repository

import com.applications.toms.depormas.data.database.local.favorite.Favorite
import com.applications.toms.depormas.data.source.favorite.LocalFavoriteDataSource

class FavoriteRepository(private val localFavoriteDataSource: LocalFavoriteDataSource) {

    fun getFavoriteList() = localFavoriteDataSource.getAllFavorite()

    suspend fun saveNewFavorite(favorite: Favorite) = localFavoriteDataSource.saveFavorite(favorite)

    suspend fun isEmpty() = localFavoriteDataSource.isEmpty()

    suspend fun deleteFavorite(favorite: Favorite) = localFavoriteDataSource.deleteFavorite(favorite)

}