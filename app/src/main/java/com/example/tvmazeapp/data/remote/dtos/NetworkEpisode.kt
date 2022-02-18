package com.example.tvmazeapp.data.remote.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkEpisode(
    var id: Int,
    var name: String,
    var season: Int,
    var number: Int,
    var summary: String,
    var image: NetworkImage
)
