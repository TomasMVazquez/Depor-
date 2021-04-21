package com.applications.toms.depormas.data.source.events

import com.applications.toms.depormas.domain.Event
import kotlinx.coroutines.flow.Flow

interface LocalEventDataSource {
    suspend fun isEmpty():Boolean
    suspend fun saveEvents(events: List<Event>)
    fun getAllEvent(): Flow<List<Event>>
}