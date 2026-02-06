package com.example.seekhoanime.data.repository

import com.example.seekhoanime.model.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    fun getTopAnime(): Flow<List<Anime>>
    suspend fun refreshTopAnime()
    suspend fun refreshTopAnimePage(page: Int)
    fun getAnimeById(id: Int): Flow<Anime?>
    suspend fun refreshAnimeDetail(id: Int)
}
