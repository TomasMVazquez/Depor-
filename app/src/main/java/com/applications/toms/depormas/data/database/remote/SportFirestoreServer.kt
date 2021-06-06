package com.applications.toms.depormas.data.database.remote

import android.util.Log
import com.applications.toms.depormas.data.source.sports.RemoteSportDataSource
import com.applications.toms.depormas.domain.Sport
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class SportFirestoreServer: RemoteSportDataSource {

    private val networkDatabase = Firebase.firestore
    private val collection = networkDatabase.collection("sports")

    override fun getSportsCollection(): Flow<List<Sport>> = callbackFlow {

        // Reference to use in Firestore
        var eventsCollection: CollectionReference? = null
        try {
            eventsCollection = collection
        } catch (e: Throwable) {
            // If Firebase cannot be initialized, close the stream of data
            // flow consumers will stop collecting and the coroutine will resume
            close(e)
        }

        // Registers callback to firestore, which will be called on new events
        val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
            if (snapshot == null) { return@addSnapshotListener }
            // Sends events to the flow! Consumers will get the new events
            try {
                offer(snapshot.toObjects(Sport::class.java))
                close()
            } catch (e: Throwable) {
                // Event couldn't be sent to the flow
                Log.d("TOMAS", "getSportsCollection: ${e.message}")
            }
        }

        // The callback inside awaitClose will be executed when the flow is
        // either closed or cancelled.
        // In this case, remove the callback from Firestore
        awaitClose { subscription?.remove() }
    }


}