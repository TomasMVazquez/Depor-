package com.applications.toms.depormas.data.database.local.favorite

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

     @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: Favorite)

    @Query("SELECT * FROM favorite_table ORDER BY id ASC")
    fun getAll(): Flow<List<Favorite>>

    @Query("SELECT COUNT(id) FROM favorite_table WHERE eventId = :eventId")
    fun findFavorite(eventId: String): Int

    @Query("SELECT COUNT(id) FROM favorite_table")
    fun favoriteCount(): Int

    @Query("DELETE FROM favorite_table WHERE eventId = :eventId")
    fun deleteFavorite(eventId: String)
}