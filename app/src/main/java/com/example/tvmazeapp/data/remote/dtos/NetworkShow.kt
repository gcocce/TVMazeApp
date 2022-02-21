package com.example.tvmazeapp.data.remote.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkShow(
    var id: Int,
    var url: String,
    var name: String,
    var status: String,
    var type: String?,
    var genres: List<String>,
    var language: String?,
    var summary: String?,
    var schedule: NetworkSchedule?,
    var image: NetworkImage?
)
