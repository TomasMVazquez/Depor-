package com.applications.toms.depormas.data.source.database

import com.applications.toms.depormas.data.model.Sport
import com.applications.toms.depormas.data.model.asDatabaseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomDataSource(database: SportDatabase): LocalDataSource {

    private val sportDao = database.sportDao

    override suspend fun isEmpty(): Boolean = sportDao.sportsCount() <= 0

    override suspend fun saveSports(sports: Flow<List<Sport>>) {
        sports.map { list ->
            sportDao.insertAllSports(*list.asDatabaseModel())
        }
    }

    override fun getAllSports(): Flow<List<Sport>> = sportDao.getAll().map { sports -> sports.map { it.asDomainModel() } }

}