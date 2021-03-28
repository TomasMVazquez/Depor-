package com.applications.toms.depormas.data.source

import com.applications.toms.depormas.data.model.Sport
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun getSportsCollection(): CollectionReference
}