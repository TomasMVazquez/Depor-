package com.applications.toms.depormas.data.database.local.sport

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.applications.toms.depormas.R
import com.applications.toms.depormas.data.database.local.event.Event

@Database(entities = [Sport::class],version = 1,exportSchema = false)
abstract class SportDatabase: RoomDatabase() {
    abstract val sportDao: SportDao

    companion object{
        fun getInstance(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            SportDatabase::class.java,
            context.getString(R.string.database_table_sport)
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}