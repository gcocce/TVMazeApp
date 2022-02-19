package com.example.tvmazeapp.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShowImage(
    var medium: String,
    var original: String
): Parcelable
