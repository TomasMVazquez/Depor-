package com.applications.toms.depormas.data.database.local

import com.applications.toms.depormas.data.database.local.event.EventDatabase
import com.applications.toms.depormas.data.database.local.event.toDomainModel
import com.applications.toms.depormas.data.source.events.LocalEventDataSource
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.domain.toDatabaseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomEventDataSource(db: EventDatabase): LocalEventDataSource {

    private val eventDao = db.eventDao

    override suspend fun isEmpty(): Boolean = eventDao.eventsCount() <= 0

    override suspend fun saveEvents(events: List<Event>) {
        eventDao.insertAllEvents(*events.toDatabaseModel())
    }

    override fun getAllEvent(): Flow<List<Event>> = eventDao.getAll().map { it.toDomainModel() }
}