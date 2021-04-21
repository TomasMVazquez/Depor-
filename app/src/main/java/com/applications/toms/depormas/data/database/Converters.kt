package com.applications.toms.depormas.data.database

import androidx.room.TypeConverter
import com.applications.toms.depormas.domain.Location
import com.applications.toms.depormas.domain.Sport
import com.google.gson.Gson
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp( value: Long?): Date = Date(value ?: 0)

    @TypeConverter
    fun dateToTimestamp(date : Date?): Long = date?.time ?: 0

    @TypeConverter
    fun locationToJson(value: Location): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToLocation(value: String): Location = Gson().fromJson(value, Location::class.java)

    @TypeConverter
    fun sportToJson(value: Sport): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToSport(value: String): Sport = Gson().fromJson(value, Sport::class.java)
}