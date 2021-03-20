package com.applications.toms.depormas.data.source

import com.applications.toms.depormas.data.model.Sport
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun getSports(): Flow<List<Sport>>
}