package com.example.tvmazeapp.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episode(
    var id: Int,
    var name: String,
    var season: Int,
    var number: Int,
    var summary: String,
    var image: ShowImage
): Parcelable
