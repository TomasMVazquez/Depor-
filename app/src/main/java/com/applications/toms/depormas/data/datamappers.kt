package com.applications.toms.depormas.data

import com.applications.toms.depormas.data.database.local.Sport as RoomSport
import com.applications.toms.depormas.domain.Sport as DomainSport

fun List<DomainSport>.toDatabaseModel(): Array<RoomSport>{
    return map{
        RoomSport(
                id = it.id,
                name = it.name,
                img = it.img,
                max_players = it.max_players
        )
    }.toTypedArray()
}

fun List<RoomSport>.toDomainModel(): List<DomainSport>{
    return map{
        DomainSport(
                id = it.id,
                name = it.name,
                img = it.img,
                max_players = it.max_players
        )
    }
}