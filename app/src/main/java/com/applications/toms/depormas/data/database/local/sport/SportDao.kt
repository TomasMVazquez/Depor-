package com.applications.toms.depormas.data.database.local.sport

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSports(vararg sports: Sport)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSport(sports: Sport)

    @Query("SELECT * FROM sport_table ORDER BY id ASC")
    fun getAll(): Flow<List<Sport>>

    @Query("SELECT COUNT(id) FROM sport_table")
    suspend fun sportsCount(): Int
}