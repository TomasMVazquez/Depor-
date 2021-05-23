package com.applications.toms.depormas.data.database.remote

import com.applications.toms.depormas.data.source.sports.RemoteSportDataSource
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.domain.Sport
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking

class SportFirestoreServer: RemoteSportDataSource {

    private val networkDatabase = Firebase.firestore
    private val collection = networkDatabase.collection(SPORT)

    override fun getSportList(): List<Sport> {
        val sportList = mutableListOf<Sport>()
        runBlocking {
            collection.addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                val list: MutableList<Sport>? = value?.toObjects(Sport::class.java)
                list?.toList()?.let { listToSave ->
                    sportList.addAll(listToSave)
                }
            }
        }
        return sportList
    }

    companion object{
        private const val SPORT = "sports"
    }
}