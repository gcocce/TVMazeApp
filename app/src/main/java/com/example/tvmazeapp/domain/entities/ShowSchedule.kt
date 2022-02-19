package com.example.tvmazeapp.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShowSchedule(
    var time: String,
    var days: List<String>
): Parcelable
