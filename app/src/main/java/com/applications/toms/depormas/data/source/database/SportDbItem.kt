package com.applications.toms.depormas.data.source.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.applications.toms.depormas.data.model.Sport

@Entity(tableName = "sport_table")
data class SportDbItem(
    @PrimaryKey
    val id: Int?,
    val name: String?,
    val img: String?,
    val max_players: Int?
)

fun SportDbItem.asDomainModel(): Sport {
    return Sport(id, name, img, max_players)
}

fun List<SportDbItem>.asDomainModel(): List<Sport>{
    return map{
        Sport(
            id = it.id,
            name = it.name,
            img = it.img,
            max_players = it.max_players
        )
    }
}