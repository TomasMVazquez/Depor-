package com.applications.toms.depormas.data.database.local.event

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

     @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEvents(vararg events: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(events: Event)

    @Query("SELECT * FROM event_table ORDER BY created_date ASC")
    fun getAll(): List<Event>

    @Query("SELECT COUNT(id) FROM event_table")
    suspend fun eventsCount(): Int

    @Update
    suspend fun updateEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)
}