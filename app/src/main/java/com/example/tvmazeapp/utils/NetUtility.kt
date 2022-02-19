package com.example.tvmazeapp.utils

import com.example.tvmazeapp.TVMazeApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.InetAddress

object NetUtility {

    fun isInternetAvailable(): Boolean {
        return try {
            Timber.d("%s isInternetAvailable", TVMazeApp().TAG)
            val ipAddr: InetAddress = InetAddress.getByName("google.com")

            Timber.d("%s isInternetAvailable %s", TVMazeApp().TAG, ipAddr)

            !ipAddr.equals("")
        } catch (e: Exception) {
            Timber.d("%s isInternetAvailable %s", TVMazeApp().TAG, e.toString())
            false
        }
    }

}