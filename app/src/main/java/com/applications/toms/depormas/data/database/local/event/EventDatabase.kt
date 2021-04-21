package com.applications.toms.depormas.data.database.local.event

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.applications.toms.depormas.R

@Database(entities = [Event::class],version = 1,exportSchema = false)
abstract class EventDatabase: RoomDatabase() {
    abstract val eventDao: EventDao

    companion object{
        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getInstance(context: Context): EventDatabase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EventDatabase::class.java,
                        context.getString(R.string.database_table_event)
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}