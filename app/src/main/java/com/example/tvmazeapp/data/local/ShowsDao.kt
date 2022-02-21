package com.example.tvmazeapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tvmazeapp.data.local.entities.DBShow

@Dao
interface ShowsDao {
    @Query("SELECT * FROM favorites Order By name Asc")
    fun getAllShows() : LiveData<List<DBShow>>

    @Query("SELECT * FROM favorites WHERE id = :id")
    fun getShow(id: Int): LiveData<DBShow>

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE id = :id)")
    fun isShowFavorite(id : Int) : Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dbShowList: List<DBShow>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dbShow: DBShow)

    @Delete
    suspend fun delete(dbShow: DBShow)
}
