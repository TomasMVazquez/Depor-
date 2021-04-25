package com.applications.toms.depormas.data.database.local.sport

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sport_table")
data class Sport(
    @PrimaryKey
    val id: Int,
    val name: String,
    val img: String,
    val max_players: Int
)

