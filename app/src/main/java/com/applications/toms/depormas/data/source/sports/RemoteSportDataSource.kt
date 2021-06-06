package com.applications.toms.depormas.data.source.sports

import com.applications.toms.depormas.domain.Sport
import kotlinx.coroutines.flow.Flow

interface RemoteSportDataSource {
    fun getSportsCollection(): Flow<List<Sport>>
}