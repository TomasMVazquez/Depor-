package com.applications.toms.depormas.data.database.local

import com.applications.toms.depormas.data.database.local.sport.SportDatabase
import com.applications.toms.depormas.data.toDatabaseModel
import com.applications.toms.depormas.domain.Sport as DomainSport
import com.applications.toms.depormas.data.source.sports.LocalSportDataSource
import com.applications.toms.depormas.data.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomSportDataSource(db: SportDatabase): LocalSportDataSource {

    private val sportDao = db.sportDao

    override suspend fun isEmpty(): Boolean = sportDao.sportsCount() <= 0

    override suspend fun saveSports(sports: List<DomainSport>) {
        sportDao.insertAllSports(*sports.toDatabaseModel())
    }

    override fun getAllSports(): Flow<List<DomainSport>> = sportDao.getAll().map { it.toDomainModel() }

}