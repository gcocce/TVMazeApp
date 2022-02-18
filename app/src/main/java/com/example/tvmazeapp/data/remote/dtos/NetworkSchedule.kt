package com.example.tvmazeapp.data.remote.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkSchedule(
    var time: String,
    var days: List<String>
)
