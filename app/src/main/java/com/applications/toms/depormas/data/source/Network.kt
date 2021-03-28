package com.applications.toms.depormas.data.source

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Network: RemoteDataSource {

    private val networkDatabase = Firebase.firestore

    override fun getSportsCollection() = networkDatabase.collection("sports")

    companion object{
        private const val TAG = "Network"
    }

}