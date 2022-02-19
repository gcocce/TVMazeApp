package com.example.tvmazeapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvmazeapp.data.repositories.TVMazeRepository
import com.example.tvmazeapp.domain.entities.Episode
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

    private val _selectedShow = MutableLiveData<Show>()
    val selectedShow: LiveData<Show> = _selectedShow

    private val _shows = MutableLiveData<ArrayList<Show>>()
    val shows: LiveData<ArrayList<Show>> = _shows

    private val _episodes = MutableLiveData<ArrayList<Episode>>()
    val episodes: LiveData<ArrayList<Episode>> = _episodes

    init {
        viewModelScope.launch {
            _shows.value = repository.loadRemoteShows()
        }
    }

    fun setSelectedShow(show: Show){
        _selectedShow.postValue(show)
    }

    fun loadEpisodes(showId: Int){
        viewModelScope.launch {
            _episodes.value = repository.loadRemoteEpisodes(showId)
        }
    }

}