package com.example.rickandmorty_mvvm_coroutines_livedata_hilt_testing_app.di

import android.content.Context
import com.example.tvmazeapp.data.remote.ApiTvMaze
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule{

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi) : Retrofit = Retrofit.Builder()
        .baseUrl("https://api.tvmaze.com/")
        //.client(provideOkHttpClient(provideHeaderInterceptor()))
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
        fun privideApiClient(retrofit: Retrofit): ApiTvMaze {
        return retrofit.create(ApiTvMaze::class.java)
    }

    private val READ_TIMEOUT = 30
    private val WRITE_TIMEOUT = 30
    private val CONNECTION_TIMEOUT = 15
    private val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB

    @Provides
    @Singleton
    fun provideOkHttpCache(@ApplicationContext context: Context): Cache {
        val cacheSize = CACHE_SIZE_BYTES // 10 MB
        return Cache(context.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headerInterceptor: Interceptor, cache: Cache
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient().newBuilder()
        okHttpClientBuilder.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.cache(cache)
        okHttpClientBuilder.addInterceptor(headerInterceptor)
        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor {
            val requestBuilder = it.request().newBuilder()
            // you can add headers by calling 'requestBuilder.addHeader(name ,  value)'
            it.proceed(requestBuilder.build())
        }
    }
}

