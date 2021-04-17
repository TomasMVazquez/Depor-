package com.applications.toms.depormas.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
data class Event(
        val id: Int,
        val event_name: String,
        val date: String,
        val time: String,
        val sport: Sport,
        val max_players: Int,
        val location: Location,
        val created_date: Timestamp = Timestamp(System.currentTimeMillis()),
        var participants: Int = 0
): Parcelable {

    fun addParticipant() = participants++

    fun removeParticipant() = participants--

    fun getDateTime(): String = "$date - ${time}hs"
}