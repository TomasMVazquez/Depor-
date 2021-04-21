package com.applications.toms.depormas.data.database.remote

import com.applications.toms.depormas.domain.Location
import com.applications.toms.depormas.domain.Sport
import java.sql.Timestamp

data class Event(
        val id: String?,
        val event_name: String,
        val date: String,
        val time: String,
        val sport: Sport,
        val max_players: Int,
        val location: Location,
        val created_date: Timestamp = Timestamp(System.currentTimeMillis()),
        var participants: Int = 0
)