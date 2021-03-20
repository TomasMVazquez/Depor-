package com.applications.toms.depormas.data.source.database

import android.content.Context
import com.applications.toms.depormas.data.model.Sport
import com.applications.toms.depormas.data.model.asDatabaseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomDataSource(context: Context): LocalDataSource {

    private val sportDao = SportDatabase.getInstance(context).sportDao

    override suspend fun isEmpty(): Boolean = sportDao.sportsCount() <= 0

    override fun saveSports(sports: Flow<List<Sport>>) {
        sports.map { list->
            sportDao.insertAllSports(*list.asDatabaseModel())
        }
    }

    override fun getAllSports(): Flow<List<Sport>> = sportDao.getAll().map { sports -> sports.map{ it.asDomainModel() } }

}