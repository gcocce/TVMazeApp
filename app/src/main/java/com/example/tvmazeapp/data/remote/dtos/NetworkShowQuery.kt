package com.example.tvmazeapp.data.remote.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkShowQuery(
    var score: Float,
    var show: NetworkShow
)
