package com.applications.toms.depormas.data.source.events

import com.applications.toms.depormas.domain.Event
import com.google.firebase.firestore.CollectionReference

interface RemoteEventDataSource {
    fun getEventsCollection(): CollectionReference
}