package com.example.tvmazeapp.data.repositories

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.MutableLiveData
import com.example.tvmazeapp.data.local.ShowDao
import com.example.tvmazeapp.data.remote.ApiTvMaze
import com.example.tvmazeapp.data.remote.ResultWrapper
import com.example.tvmazeapp.data.remote.dtos.NetworkEpisode
import com.example.tvmazeapp.data.remote.dtos.NetworkShow
import com.example.tvmazeapp.data.remote.dtos.NetworkShowQuery
import com.example.tvmazeapp.data.remote.mappers.NetworkEpisodeMapper
import com.example.tvmazeapp.data.remote.mappers.NetworkShowMapper
import com.example.tvmazeapp.data.remote.safeApiCall
import com.example.tvmazeapp.domain.entities.Episode
import com.example.tvmazeapp.domain.entities.Show
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TVMazeRepository @Inject constructor(
    private val remoteDataSource: ApiTvMaze,
    private val localDataSource: ShowDao
) {

    suspend fun searchShowRemote(query: String): ResultWrapper<List<NetworkShowQuery>> {
        return safeApiCall(Dispatchers.IO){
            remoteDataSource.searchShow(query)
        }
    }

    suspend fun loadRemoteShows(page: Int): ResultWrapper<List<NetworkShow>> {
            return safeApiCall(Dispatchers.IO){
                remoteDataSource.getShows(page)
            }
    }

    suspend fun loadRemoteEpisodes(showId: Int): ResultWrapper<List<NetworkEpisode>> {
        return safeApiCall(Dispatchers.IO) {
            remoteDataSource.getShowEpisodes(showId)
        }
    }

}