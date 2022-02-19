package com.example.tvmazeapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvmazeapp.data.repositories.TVMazeRepository
import com.example.tvmazeapp.domain.entities.Episode
import com.example.tvmazeapp.domain.entities.SeasonList
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.domain.usecases.GetShowsNextPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowsViewModel @Inject constructor(
    private val repository: TVMazeRepository,
    private val showsNextPage: GetShowsNextPage
): ViewModel(){

    private val _progressLoadingShows = MutableLiveData<Boolean>()
    val progressLoadingShows: LiveData<Boolean> = _progressLoadingShows

    private val _progressLoadingEpisodes = MutableLiveData<Boolean>()
    val progressLoadingEpisodes: LiveData<Boolean> = _progressLoadingEpisodes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _selectedShow = MutableLiveData<Show>()
    val selectedShow: LiveData<Show> = _selectedShow

    private val _shows = MutableLiveData<ArrayList<Show>>()
    val shows: LiveData<ArrayList<Show>> = _shows

    private val _episodes = MutableLiveData<ArrayList<Episode>>()
    val episodes: LiveData<ArrayList<Episode>> = _episodes

    private val _selectedEpisode = MutableLiveData<Episode>()
    val selectedEpisode: LiveData<Episode> = _selectedEpisode

    //private val _episodeList = MutableLiveData<SeasonList>()
    //val seasonList: LiveData<SeasonList> = _episodeList

    init {
        viewModelScope.launch {
            val showList = repository.loadRemoteShows()
            _shows.value = showList
            _progressLoadingShows.postValue(false)
        }
    }

    fun setSelectedShow(show: Show){
        _selectedShow.postValue(show)
    }

    fun setSelectedEpisode(episode: Episode){
        _selectedEpisode.postValue(episode)
    }

    fun loadEpisodes(showId: Int){
        viewModelScope.launch {
            val episodeList = repository.loadRemoteEpisodes(showId)
            _episodes.value = episodeList
            _progressLoadingEpisodes.value = false
        }
    }

}