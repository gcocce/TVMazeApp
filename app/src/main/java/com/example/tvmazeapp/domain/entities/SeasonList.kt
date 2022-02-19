package com.example.tvmazeapp.domain.entities

data class SeasonList(
    val seasons: List<Season>
){
    data class Season(
        val name: String,
        val episodes: List<Episode>
    )
}
