package com.applications.toms.depormas.data.source

import com.applications.toms.depormas.data.model.Sport
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getSports(): List<Sport>
}