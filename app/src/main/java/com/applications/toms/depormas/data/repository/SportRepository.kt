package com.applications.toms.depormas.data.repository

import com.applications.toms.depormas.data.source.sports.LocalSportDataSource
import com.applications.toms.depormas.data.source.sports.RemoteSportDataSource
import com.applications.toms.depormas.domain.Sport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SportRepository(
        private val localDataSource: LocalSportDataSource,
        private val remoteDataSource: RemoteSportDataSource
) {

    fun getSports(): Flow<List<Sport>> {
        refreshSports()
        return localDataSource.getAllSports()
    }

    private fun refreshSports(){
        remoteDataSource.getSportsCollection().addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            val list: MutableList<Sport>? = value?.toObjects(Sport::class.java)
            list?.toList()?.let { listToSave ->
                runBlocking {
                    localDataSource.saveSports(listToSave)
                }
            }
        }
    }
}