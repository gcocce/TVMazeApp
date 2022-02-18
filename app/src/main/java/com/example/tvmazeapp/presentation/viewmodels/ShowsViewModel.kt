package com.example.tvmazeapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvmazeapp.data.repositories.TVMazeRepository
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

    private val _shows = MutableLiveData<ArrayList<Show>>()
    val shows: LiveData<ArrayList<Show>> = _shows

    //val shows = repository.shows
    init {
        viewModelScope.launch {
            // suspend and resume make this database request main-safe
            _shows.value = repository.loadRemoteShows()
        }
    }


}