package com.example.tvmazeapp.data.remote.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkImage(
    var medium: String,
    var original: String
)
