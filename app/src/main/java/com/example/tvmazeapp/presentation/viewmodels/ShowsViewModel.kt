package com.example.tvmazeapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvmazeapp.data.remote.ResultWrapper
import com.example.tvmazeapp.data.remote.mappers.NetworkEpisodeMapper
import com.example.tvmazeapp.data.remote.mappers.NetworkShowMapper
import com.example.tvmazeapp.data.remote.mappers.NetworkShowQueryMapper
import com.example.tvmazeapp.data.repositories.TVMazeRepository
import com.example.tvmazeapp.domain.entities.Episode
import com.example.tvmazeapp.domain.entities.Show
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

enum class Mode {LIST, SEARCH, FAVORITES}

@HiltViewModel
class ShowsViewModel @Inject constructor(
    private val repository: TVMazeRepository
): ViewModel(){

    var mode:Mode = Mode.LIST

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _progressLoadingShows = MutableLiveData<Boolean>()
    val progressLoadingShows: LiveData<Boolean> = _progressLoadingShows

    private val _progressLoadingEpisodes = MutableLiveData<Boolean>()
    val progressLoadingEpisodes: LiveData<Boolean> = _progressLoadingEpisodes

    private val _selectedShow = MutableLiveData<Show>()
    val selectedShow: LiveData<Show> = _selectedShow

    private val _selectedShowIsFavorite = MutableLiveData<Boolean>()
    val selectedShowIsFavorite: LiveData<Boolean> = _selectedShowIsFavorite

    private val _shows = MutableLiveData<ArrayList<Show>>()
    val shows: LiveData<ArrayList<Show>> = _shows

    private val _episodes = MutableLiveData<ArrayList<Episode>>()
    val episodes: LiveData<ArrayList<Episode>> = _episodes

    private val _selectedEpisode = MutableLiveData<Episode>()
    val selectedEpisode: LiveData<Episode> = _selectedEpisode

    val favorites: LiveData<List<Show>>
        get() {
            return repository.favorites
        }

    private var currentPage = 0
    private var downloadingNextPage = false

    init {
        loadShowsPage(currentPage)
    }

    fun setSelectedShow(show: Show){
        _selectedShow.postValue(show)

        viewModelScope.launch {
            selectedShowIsFavorite

            val isFavorite = repository.isFavorite(show)
            _selectedShowIsFavorite.postValue(isFavorite)
        }
    }

    fun setSelectedEpisode(episode: Episode){
        _selectedEpisode.postValue(episode)
    }

    fun switchToFullList(){
        if (mode!=Mode.LIST){
            mode=Mode.LIST

            _shows.value = ArrayList<Show>()
            _progressLoadingShows.postValue(true)

            currentPage = 0
            loadShowsPage(currentPage)
        }
    }

    fun switchToFavorites(){
        if(mode != Mode.FAVORITES){
            mode = Mode.FAVORITES
            _shows.value = ArrayList<Show>()
        }
    }

    fun nextPage(){
        if (mode==Mode.LIST){
            if(!downloadingNextPage){
                currentPage +=1
                downloadingNextPage = true
                loadShowsPage(currentPage)
            }
        }
    }

    fun addShowAsFavorites(){
        viewModelScope.launch {
            val currentSelectedShow = selectedShow.value
            if (currentSelectedShow!=null){
                Timber.d("Add show to favorites %s", currentSelectedShow.name)
                repository.addFavorite(currentSelectedShow)
                _selectedShowIsFavorite.postValue(true)
            }
        }
    }

    fun removeFromFavorites(){
        viewModelScope.launch {
            val currentSelectedShow = selectedShow.value
            if(currentSelectedShow!=null){
                repository.removeFromFavorites(currentSelectedShow)
                _selectedShowIsFavorite.postValue(false)
            }
        }
    }

    fun searchShowRemote(query: String){
        mode = Mode.SEARCH

        _shows.value = ArrayList<Show>()
        _progressLoadingShows.postValue(true)

        Timber.d("ShowsViewModel searchShowRemote %s", query)

        viewModelScope.launch {
            val remoteQueryResponse = repository.searchShowRemote(query)
            when (remoteQueryResponse) {
                is ResultWrapper.NetworkError -> {
                    Timber.d("remoteQueryResponse NetworkError")
                    _error.postValue("Network Error")
                }
                is ResultWrapper.GenericError -> {
                    Timber.d("remoteQueryResponse GenericError ${remoteQueryResponse}")
                        _error.postValue("Error $remoteQueryResponse")
                }
                is ResultWrapper.Success -> {
                    val shows = NetworkShowQueryMapper().mapFromEntityList(remoteQueryResponse.value)
                        _shows.value = ArrayList(shows)
                }
            }
            _progressLoadingShows.postValue(false)
        }
    }

    fun loadShowsPage(page: Int){
        Timber.d("ShowsViewModel loadShows %s", page)
        viewModelScope.launch {
            val remoteShowsResponse = repository.loadRemoteShows(page)
            when (remoteShowsResponse) {
                is ResultWrapper.NetworkError -> {
                    Timber.d("remoteShowsResponse NetworkError")
                    _error.postValue("Network Error")
                }
                is ResultWrapper.GenericError -> {
                    Timber.d("remoteShowsResponse GenericError ${remoteShowsResponse}")

                    // TVMaze respondes 404 when there are no more pages to retrieve
                    if (remoteShowsResponse?.code!=404){
                        _error.postValue("Error $remoteShowsResponse")
                    }
                }
                is ResultWrapper.Success -> {
                    val shows = NetworkShowMapper().mapFromEntityList(remoteShowsResponse.value)
                    if (_shows.value.isNullOrEmpty()){
                        _shows.value = ArrayList(shows)
                    }else{
                        _shows.value!!.addAll(shows)
                    }
                    downloadingNextPage = false
                    Timber.d("ShowsViewModel downloading nextPage false")
                }
            }
            _progressLoadingShows.postValue(false)
        }
    }

    fun loadEpisodes(showId: Int){
        viewModelScope.launch {
            val remoteEpisodesResponse = repository.loadRemoteEpisodes(showId)
            when (remoteEpisodesResponse) {
                is ResultWrapper.NetworkError -> {
                    Timber.d("remoteShowsResponse NetworkError")
                    _error.postValue("Network Error")
                }
                is ResultWrapper.GenericError -> {
                    _error.postValue("Error $remoteEpisodesResponse")
                }
                is ResultWrapper.Success -> {
                    val episodes = NetworkEpisodeMapper().mapFromEntityList(remoteEpisodesResponse.value)
                    _episodes.value = ArrayList(episodes)
                }
            }
            _progressLoadingEpisodes.postValue(false)
        }
    }
}