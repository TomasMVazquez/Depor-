package com.applications.toms.depormas.data.database.local.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.applications.toms.depormas.data.database.Converters
import com.applications.toms.depormas.domain.Location
import com.applications.toms.depormas.domain.Sport
import com.applications.toms.depormas.domain.Event as DomainEvent
import java.util.*

@Entity(tableName = "favorite_table")
data class Favorite(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val eventId: String
)

