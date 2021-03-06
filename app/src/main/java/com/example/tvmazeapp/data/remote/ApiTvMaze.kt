package com.example.tvmazeapp.data.remote

import com.example.tvmazeapp.data.remote.dtos.NetworkEpisode
import com.example.tvmazeapp.data.remote.dtos.NetworkShow
import com.example.tvmazeapp.data.remote.dtos.NetworkShowQuery
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiTvMaze {

    @GET("shows/{id}")
    suspend fun getShow(@Path("id") id: Int): NetworkShow

    //https://api.tvmaze.com/shows?page=0
    @GET("shows")
    suspend fun getShows(@Query("page") page: Int): List<NetworkShow>

    //https://api.tvmaze.com/shows/1/episodes
    @GET("shows/{id}/episodes")
    suspend fun getShowEpisodes(@Path("id") id: Int): List<NetworkEpisode>

    //URL: /search/shows?q=:query
    @GET("search/shows")
    suspend fun searchShow(@Query("q") query: String): List<NetworkShowQuery>
}