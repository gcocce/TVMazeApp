package com.example.tvmazeapp.domain.entities

data class EpisodeList(
    val seasons: List<Season>
){
    data class Season(
        val name: String,
        val episodes: List<Episode>
    )
}
