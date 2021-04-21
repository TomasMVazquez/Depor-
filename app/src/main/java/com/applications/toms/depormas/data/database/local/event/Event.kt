package com.applications.toms.depormas.data.database.local.event

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.applications.toms.depormas.data.database.Converters
import com.applications.toms.depormas.domain.Location
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.domain.Event as DomainEvent
import java.sql.Timestamp
import java.util.*

@Entity(tableName = "event_table")
@TypeConverters(Converters::class)
data class Event(
        @PrimaryKey
        val id: String,
        val event_name: String,
        val date: String,
        val time: String,
        val sport: Sport,
        val max_players: Int,
        val location: Location,
        val created_date: Date = Date(System.currentTimeMillis()),
        var participants: Int = 0
)

fun Event.toDomainModel(): DomainEvent = DomainEvent(id, event_name, date, time, sport, max_players, location, created_date, participants)

fun List<Event>.toDomainModel(): List<DomainEvent> {
        return map {
                DomainEvent(
                        id = it.id,
                        event_name = it.event_name,
                        date = it.date,
                        time = it.time,
                        sport = it.sport,
                        max_players = it.max_players,
                        location = it.location,
                        created_date = it.created_date,
                        participants = it.participants
                )
        }
}