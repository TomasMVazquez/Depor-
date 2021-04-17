package com.applications.toms.depormas.data.source

import com.applications.toms.depormas.domain.Sport
import kotlinx.coroutines.flow.Flow


interface LocalDataSource {
    suspend fun isEmpty():Boolean
    suspend fun saveSports(sports: List<Sport>)
    fun getAllSports(): Flow<List<Sport>>
}
