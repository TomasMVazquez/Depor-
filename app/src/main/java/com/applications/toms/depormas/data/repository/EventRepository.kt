package com.applications.toms.depormas.data.repository

import com.applications.toms.depormas.data.source.events.LocalEventDataSource
import com.applications.toms.depormas.data.source.events.RemoteEventDataSource
import com.applications.toms.depormas.domain.Event
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EventRepository(
        private val localDataSource: LocalEventDataSource,
        private val remoteDataSource: RemoteEventDataSource
) {

    fun getEvents(): Flow<List<Event>> {
        refreshEvents()
        return localDataSource.getAllEvent()
    }

    private fun refreshEvents(){
        //TODO Es Correcto un run blocking?
        runBlocking {
            localDataSource.saveEvents(remoteDataSource.getEventList())
        }
    }

    fun saveEvent(event: Event): String{
        val id = remoteDataSource.getEventDocumentId()
        val newEvent = Event(
            id = id,
            event_name = event.event_name,
            date = event.date,
            time = event.time,
            sport = event.sport,
            max_players = event.max_players,
            location = event.location,
            created_date = event.created_date
        )
        remoteDataSource.saveEvent(newEvent)
        return id
    }

    fun updateEvent(event: Event){
        remoteDataSource.updateEvent(event)
    }

}