package com.applications.toms.depormas.data.database.remote

import com.applications.toms.depormas.data.source.events.RemoteEventDataSource
import com.applications.toms.depormas.domain.Event
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking

class EventFirestoreServer: RemoteEventDataSource {

    private val networkDatabase = Firebase.firestore
    private val collection = networkDatabase.collection(collectionName)

    override fun updateEvent(event: Event) {
        collection
            .document(event.id)
            .set(event, SetOptions.merge())
    }

    override fun getEventDocumentId(): String = collection.document().id

    override fun getEventList(): List<Event> {
        val eventList = mutableListOf<Event>()
        runBlocking {
            collection.addSnapshotListener { value, error ->
                if (error != null) eventList
                val list: MutableList<Event>? = value?.toObjects(Event::class.java)
                list?.toList()?.let { listToSave ->
                    eventList.addAll(listToSave)
                }
            }
        }
        return eventList
    }

    override fun saveEvent(event: Event) {
        collection.document(event.id).set(event)
    }

    companion object {
        private const val collectionName = "events"
    }

}