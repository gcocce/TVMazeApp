package com.example.tvmazeapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tvmazeapp.domain.entities.ShowImage
import com.example.tvmazeapp.domain.entities.ShowSchedule

@Entity(tableName = "favorites")
data class DBShow(
    @PrimaryKey
    val id: Int,
    var url: String,
    var name: String,
    var status: String,
    var type: String,
    var genres: String,
    var language: String,
    var summary: String,
    var schedule_time: String,
    var schedule_days: String,
    var image_medium: String,
    var image_original: String
)
