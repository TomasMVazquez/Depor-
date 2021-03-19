package com.applications.toms.depormas.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id: Int?,
    val date: String?,
    val time: String?,
    val sport: Sport?,
    val max_players: Int?,
    val location: Location?,
    val created_date: String?,
    var participants: Int = 0
): Parcelable {

    constructor() : this(null,null,null,null,null,null,null)

    fun addParticipant() = participants++
    fun removeParticipant() = participants--

}