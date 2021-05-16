package com.applications.toms.depormas.data.database.local.favorite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.applications.toms.depormas.R

@Database(entities = [Favorite::class],version = 1,exportSchema = false)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract val favoriteDao: FavoriteDao

    companion object{
        fun getInstance(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            FavoriteDatabase::class.java,
            context.getString(R.string.database_table_favorite)
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}