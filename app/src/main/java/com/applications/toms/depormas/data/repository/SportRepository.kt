package com.applications.toms.depormas.data.repository

import com.applications.toms.depormas.data.model.Sport
import com.applications.toms.depormas.data.source.RemoteDataSource
import com.applications.toms.depormas.data.source.database.LocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SportRepository(private val localDataSource: LocalDataSource, private val remoteDataSource: RemoteDataSource) {

    fun getSports(): Flow<List<Sport>> = flow<List<Sport>> {
        if(localDataSource.isEmpty()){
            val sports = remoteDataSource.getSports()
            localDataSource.saveSports(sports)
        }
        localDataSource.getAllSports()
    }

    companion object{
        private const val TAG = "SportRepository"
    }

}