package com.applications.toms.depormas.data.source.sports

import com.applications.toms.depormas.domain.Sport
import com.google.firebase.firestore.CollectionReference

interface RemoteSportDataSource {
    fun getSportList(): List<Sport>
}