package com.applications.toms.depormas.data.repository

import com.applications.toms.depormas.data.source.events.LocalEventDataSource
import com.applications.toms.depormas.data.source.events.RemoteEventDataSource
import com.applications.toms.depormas.domain.Event
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EventRepository(
        private val coroutineScope: CoroutineScope,
        private val localDataSource: LocalEventDataSource,
        private val remoteDataSource: RemoteEventDataSource
) {

    fun getEvents(): List<Event> {
        refreshEvents()
        return localDataSource.getAllEvent()
    }

    private fun refreshEvents(){
        remoteDataSource.getEventsCollection().addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            val list: MutableList<Event>? = value?.toObjects(Event::class.java)
            list?.toList()?.let { listToSave ->
                coroutineScope.launch {
                    localDataSource.saveEvents(listToSave)
                }
            }
        }
    }

    fun saveEvent(event: Event){
        val id = remoteDataSource.getEventsCollection().document().id
        remoteDataSource.getEventsCollection().document(id).set(
                Event(
                        id = id,
                        event_name = event.event_name,
                        date = event.date,
                        time = event.time,
                        sport = event.sport,
                        max_players = event.max_players,
                        location = event.location,
                        created_date = event.created_date
                )
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) refreshEvents()
        }
    }

    fun updateEvent(event: Event){
        remoteDataSource.getEventsCollection().document(event.id!!)
                .set(event, SetOptions.merge()).addOnCompleteListener { task ->
                    if (task.isSuccessful) refreshEvents()
                }
    }

}