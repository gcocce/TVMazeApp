package com.example.tvmazeapp.data.repositories

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.RoomDatabase
import com.example.tvmazeapp.data.local.ShowsDao
import com.example.tvmazeapp.data.local.mappers.DBShowMapper
import com.example.tvmazeapp.data.local.mappers.DBShowMapper_Factory
import com.example.tvmazeapp.data.remote.ApiTvMaze
import com.example.tvmazeapp.data.remote.ResultWrapper
import com.example.tvmazeapp.data.remote.dtos.NetworkEpisode
import com.example.tvmazeapp.data.remote.dtos.NetworkShow
import com.example.tvmazeapp.data.remote.dtos.NetworkShowQuery
import com.example.tvmazeapp.data.remote.safeApiCall
import com.example.tvmazeapp.domain.entities.Show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class TVMazeRepository @Inject constructor(
    private val remoteDataSource: ApiTvMaze,
    private val localDataSource: ShowsDao
) {

    val favorites: LiveData<List<Show>> = Transformations.map(localDataSource.getAllShows()){
        it.map {
            DBShowMapper().mapFromEntity(it)
        }
    }

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

    suspend fun addFavorite(show: Show){
        return withContext(Dispatchers.IO){
            try {
                localDataSource.insert(DBShowMapper().mapToEntity(show))
            } catch (e: SQLiteConstraintException) {
                Timber.e(e.message)
            }catch (e: Exception){
                Timber.e("Exception: %s", e.message)
            }
        }
    }

    suspend fun removeFromFavorites(show: Show){
        withContext(Dispatchers.IO){
            localDataSource.delete(DBShowMapper().mapToEntity(show))
        }
    }

    suspend fun isFavorite(show: Show): Boolean {
        return withContext(Dispatchers.IO) {
            localDataSource.isShowFavorite(show.id)
        }
    }
}