package com.applications.toms.depormas.usecases

import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.domain.Event

class SaveEvent(private val eventRepository: EventRepository) {
    fun invoke(event: Event){
        eventRepository.saveEvent(event)
    }
}