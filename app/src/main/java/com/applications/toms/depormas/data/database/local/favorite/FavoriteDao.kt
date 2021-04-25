package com.applications.toms.depormas.data.database.local.favorite

import androidx.room.*

@Dao
interface FavoriteDao {

     @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: Favorite)

    @Query("SELECT * FROM favorite_table ORDER BY id ASC")
    fun getAll(): List<Favorite>

    @Query("SELECT COUNT(id) FROM favorite_table")
    fun favoriteCount(): Int

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)
}