package com.applications.toms.depormas.data.database.remote

import com.applications.toms.depormas.data.source.events.RemoteEventDataSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EventFirestoreServer: RemoteEventDataSource {

    private val networkDatabase = Firebase.firestore

    override fun getEventsCollection(): CollectionReference = networkDatabase.collection("events")

}