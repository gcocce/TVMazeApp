package com.example.tvmazeapp.data.repositories

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.MutableLiveData
import com.example.tvmazeapp.data.local.ShowDao
import com.example.tvmazeapp.data.remote.ApiTvMaze
import com.example.tvmazeapp.data.remote.dtos.NetworkShow
import com.example.tvmazeapp.data.remote.mappers.NetworkShowMapper
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

    //var shows = MutableLiveData<ArrayList<Show>>()

    //suspend fun loadNews() = withContext(ioDispatcher) { /* ... */ }

    suspend fun loadRemoteShows(): ArrayList<Show> {
        return withContext(Dispatchers.IO) {
            var shows = ArrayList<Show>()
            try {
                val remoteShows = remoteDataSource.getShows(0)
                if (remoteShows.isSuccessful){
                    val bodyList = remoteShows.body()
                    if (bodyList!=null){
                        shows = ArrayList(NetworkShowMapper().mapFromEntityList(bodyList))
                    }
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }

            shows
        }
    }


}