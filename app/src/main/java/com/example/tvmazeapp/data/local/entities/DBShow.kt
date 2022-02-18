package com.example.tvmazeapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shows")
data class DBShow(
    @PrimaryKey
    val id: Int
)
