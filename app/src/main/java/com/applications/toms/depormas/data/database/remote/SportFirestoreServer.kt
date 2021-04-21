package com.applications.toms.depormas.data.database.remote

import com.applications.toms.depormas.data.source.sports.RemoteSportDataSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SportFirestoreServer: RemoteSportDataSource {

    private val networkDatabase = Firebase.firestore

    override fun getSportsCollection() = networkDatabase.collection("sports")

}