package com.example.news2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.news2.database.dao.SourceDao
import com.example.news2.database.entities.Source

@Database(entities = [Source::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract val sourceDao: SourceDao
}


private lateinit var INSTANCE: NewsDatabase


fun getDatabase(context: Context): NewsDatabase {
    synchronized(NewsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE =
                Room.databaseBuilder(context.applicationContext, NewsDatabase::class.java, "news")
                    .build()
        }
    }
    return INSTANCE
}