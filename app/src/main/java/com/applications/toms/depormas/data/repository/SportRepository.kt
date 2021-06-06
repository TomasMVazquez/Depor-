package com.applications.toms.depormas.data.repository

import com.applications.toms.depormas.data.source.sports.LocalSportDataSource
import com.applications.toms.depormas.data.source.sports.RemoteSportDataSource
import com.applications.toms.depormas.domain.Sport
import kotlinx.coroutines.flow.*

class SportRepository(
        private val localDataSource: LocalSportDataSource,
        private val remoteDataSource: RemoteSportDataSource
) {

    suspend fun getSports(): Flow<List<Sport>> {
        if (localDataSource.isEmpty()){
            remoteDataSource.getSportsCollection().collectLatest {
                localDataSource.saveSports(it)
            }
        }
        return localDataSource.getAllSports()
    }
}