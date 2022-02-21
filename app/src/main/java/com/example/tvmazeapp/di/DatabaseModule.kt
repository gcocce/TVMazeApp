package com.example.tvmazeapp.di

import android.content.Context
import com.example.tvmazeapp.data.local.AppDatabase
import com.example.tvmazeapp.data.local.ShowsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideFavoritesDao(appDatabase: AppDatabase): ShowsDao {
        return appDatabase.favoritesShowsDao()
    }

}