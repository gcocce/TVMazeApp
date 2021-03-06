package com.example.tvmazeapp.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Show(
    var id: Int,
    var url: String,
    var name: String,
    var status: String,
    var type: String,
    var genres: List<String>,
    var language: String,
    var summary: String,
    var schedule: ShowSchedule,
    var image: ShowImage
): Parcelable
