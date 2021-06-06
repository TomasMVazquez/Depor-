package com.applications.toms.depormas.usecases

import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.domain.Sport
import kotlinx.coroutines.flow.Flow

class GetSports(private val sportRepository: SportRepository) {
    suspend fun invoke(): Flow<List<Sport>> = sportRepository.getSports()
}