package com.applications.toms.depormas.data.source.database

import com.applications.toms.depormas.data.model.Sport
import kotlinx.coroutines.flow.Flow


interface LocalDataSource {
    suspend fun isEmpty():Boolean
    fun saveSports(sports: Flow<List<Sport>>)
    fun getAllSports(): Flow<List<Sport>>
}
