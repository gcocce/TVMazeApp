package com.example.tvmazeapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tvmazeapp.data.local.entities.DBShow

@Dao
interface ShowDao {
    @Query("SELECT * FROM shows")
    fun getAllShows() : LiveData<List<DBShow>>

    @Query("SELECT * FROM shows WHERE id = :id")
    fun getShow(id: Int): LiveData<DBShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(show: List<DBShow>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(show: DBShow)
}
