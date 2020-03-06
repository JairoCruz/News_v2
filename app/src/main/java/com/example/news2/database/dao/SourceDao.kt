package com.example.news2.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.news2.database.entities.Source

@Dao
interface SourceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sources: List<Source>)

    @Query("select * from Sources")
    fun getSources(): LiveData<List<Source>>

    @Update
    fun updateSource(source: Source)

}