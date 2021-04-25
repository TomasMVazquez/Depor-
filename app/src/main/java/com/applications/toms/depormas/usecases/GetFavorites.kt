package com.applications.toms.depormas.usecases

import com.applications.toms.depormas.data.database.local.favorite.Favorite
import com.applications.toms.depormas.data.repository.FavoriteRepository

class GetFavorites(private val favoriteRepository: FavoriteRepository) {
    fun invoke(): List<Favorite> = favoriteRepository.getFavoriteList()
}