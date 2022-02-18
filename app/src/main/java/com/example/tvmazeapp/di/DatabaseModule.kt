package com.example.tvmazeapp.di

import android.content.Context
import com.example.tvmazeapp.data.local.AppDatabase
import com.example.tvmazeapp.data.local.ShowDao
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
    fun provideCharacterDao(appDatabase: AppDatabase): ShowDao {
        return appDatabase.showDao()
    }

}