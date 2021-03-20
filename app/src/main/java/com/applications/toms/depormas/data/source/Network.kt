package com.applications.toms.depormas.data.source

import android.util.Log
import com.applications.toms.depormas.data.model.Sport
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class Network: RemoteDataSource {

    private val networkDatabase = Firebase.firestore

    override fun getSports(): Flow<List<Sport>> {
        return callbackFlow {
            val listenerRegistration = networkDatabase.collection("sports")
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(message = "Error fetching posts",
                            cause = firebaseFirestoreException)
                        return@addSnapshotListener
                    }
                    val map = querySnapshot?.documents?.mapNotNull {
                        it.toObject(Sport::class.java)
                    }
                    map?.let { offer(it) }
                    //offer(map)
                }
            awaitClose {
                Log.d(TAG, "Cancelling posts listener")
                listenerRegistration.remove()
            }
        }
    }

    companion object{
        private const val TAG = "Network"
    }

}