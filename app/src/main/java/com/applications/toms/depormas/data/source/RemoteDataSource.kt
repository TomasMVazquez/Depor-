package com.applications.toms.depormas.data.source

import com.google.firebase.firestore.CollectionReference

interface RemoteDataSource {
    fun getSportsCollection(): CollectionReference
}