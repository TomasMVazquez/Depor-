package com.applications.toms.depormas.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*
import com.applications.toms.depormas.data.database.local.event.Event as EventDatabase

@Parcelize
data class Event(
        val id: String = "",
        val event_name: String = "",
        val date: String = "",
        val time: String = "",
        val sport: Sport = Sport(),
        val max_players: Int = 0,
        val location: Location = Location(),
        val created_date: Date = Date(System.currentTimeMillis()),
        var participants: Int = 1
): Parcelable {

    fun addParticipant() = participants++

    fun removeParticipant() = participants--

    fun getDateTime(): String = "$date - ${time}hs"
}

fun Event.toDatabaseModel(): EventDatabase = EventDatabase(id, event_name, date, time, sport, max_players, location, created_date, participants)

fun List<Event>.toDatabaseModel(): Array<EventDatabase> {
    return map {
        EventDatabase(
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
    }.toTypedArray()
}

fun List<Event>.filterBySport(sportId: Int): List<Event> {
    return if (sportId != -1) {
        filter { it.sport.id == sportId }
    }else{
        this
    }
}