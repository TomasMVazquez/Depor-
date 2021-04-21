package com.applications.toms.depormas.data.source.sports

import com.applications.toms.depormas.domain.Sport
import kotlinx.coroutines.flow.Flow


interface LocalSportDataSource {
    suspend fun isEmpty():Boolean
    suspend fun saveSports(sports: List<Sport>)
    fun getAllSports(): Flow<List<Sport>>
}
