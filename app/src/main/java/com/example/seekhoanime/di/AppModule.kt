package com.example.seekhoanime.di

import android.content.Context
import androidx.room.Room
import com.example.seekhoanime.data.local.AnimeDao
import com.example.seekhoanime.data.local.AppDatabase
import com.example.seekhoanime.data.remote.JikanService
import com.example.seekhoanime.data.repository.AnimeRepository
import com.example.seekhoanime.data.repository.AnimeRepositoryImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindRepository(impl: AnimeRepositoryImpl): AnimeRepository

    companion object {
        private const val BASE_URL = "https://api.jikan.moe"

        @Provides
        @Singleton
        fun provideGson(): Gson = GsonBuilder().create()

        @Provides
        @Singleton
        fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC

            val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
            val cacheDir = File(context.cacheDir, "http_cache")
            val cache = Cache(cacheDir, cacheSize)

            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .cache(cache)
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        @Provides
        @Singleton
        fun provideJikanService(retrofit: Retrofit): JikanService = retrofit.create(JikanService::class.java)

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "seekho_anime_db").build()
        }

        @Provides
        fun provideAnimeDao(db: AppDatabase): AnimeDao = db.animeDao()
    }
}
