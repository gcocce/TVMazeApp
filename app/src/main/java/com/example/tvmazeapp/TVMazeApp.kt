package com.example.tvmazeapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@HiltAndroidApp
class TVMazeApp: Application() {

    val TAG = "TVMazeAPP"

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
}