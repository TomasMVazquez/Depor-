package com.applications.toms.depormas.data.source.database

import android.content.Context
import com.applications.toms.depormas.data.model.Sport
import com.applications.toms.depormas.data.model.asDatabaseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomDataSource(context: Context): LocalDataSource {

    private val sportDao = SportDatabase.getInstance(context).sportDao

    override suspend fun isEmpty(): Boolean = sportDao.sportsCount() <= 0

    override suspend fun saveSports(sports: List<Sport>) {
        sportDao.insertAllSports(*sports.asDatabaseModel())
    }

    override suspend fun getAllSports(): List<Sport> = sportDao.getAll().asDomainModel()

}