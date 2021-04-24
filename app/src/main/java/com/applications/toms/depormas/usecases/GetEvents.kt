package com.applications.toms.depormas.usecases

import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.domain.Event
import kotlinx.coroutines.flow.Flow

class GetEvents(private val eventRepository: EventRepository) {
    fun invoke(): List<Event> = eventRepository.getEvents()
}