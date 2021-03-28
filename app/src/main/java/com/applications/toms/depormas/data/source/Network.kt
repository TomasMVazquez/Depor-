package com.applications.toms.depormas.data.source

import android.util.Log
import androidx.lifecycle.asLiveData
import com.applications.toms.depormas.data.model.Sport
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class Network: RemoteDataSource {

    private val networkDatabase = Firebase.firestore

    override fun getSportsCollection() = networkDatabase.collection("sports")

    companion object{
        private const val TAG = "Network"
    }

}