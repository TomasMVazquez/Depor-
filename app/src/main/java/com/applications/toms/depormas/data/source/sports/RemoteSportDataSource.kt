package com.applications.toms.depormas.data.source.sports

import com.google.firebase.firestore.CollectionReference

interface RemoteSportDataSource {
    fun getSportsCollection(): CollectionReference
}