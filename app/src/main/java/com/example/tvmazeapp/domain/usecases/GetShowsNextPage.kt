package com.example.tvmazeapp.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class GetShowsNextPage @Inject constructor(
    //private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO){
        // Long-running blocking operations happen on a background thread.


    }
}