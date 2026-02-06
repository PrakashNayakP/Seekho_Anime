package com.example.seekhoanime.data.repository

import android.util.Log
import com.example.seekhoanime.data.local.AnimeDao
import com.example.seekhoanime.data.remote.JikanService
import com.example.seekhoanime.data.remote.dto.AnimeDto
import com.example.seekhoanime.model.Anime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepositoryImpl @Inject constructor(
    private val dao: AnimeDao,
    private val service: JikanService
) : AnimeRepository {

    override fun getTopAnime(): Flow<List<Anime>> {
        return dao.getAllAnime()
    }

    override suspend fun refreshTopAnime() {
        val resp = service.getTopAnime(1)
        if (resp.isSuccessful) {
            val body = resp.body()
            val list = body?.data?.map { it.safeToAnime() } ?: emptyList()
            dao.clearAll()
            dao.insertAll(list)
        }
    }

    override suspend fun refreshTopAnimePage(page: Int) {
        val resp = service.getTopAnime(page)
        if (resp.isSuccessful) {
            val body = resp.body()
            val list = body?.data?.map { it.safeToAnime() } ?: emptyList()
            dao.insertAll(list)
        }
    }

    override fun getAnimeById(id: Int): Flow<Anime?> {
        return dao.getAnimeById(id)
    }

    override suspend fun refreshAnimeDetail(id: Int) {
        val resp = service.getAnimeDetail(id)
        if (resp.isSuccessful) {
            val body = resp.body()
            val dto = body?.data
            dto?.let {
                val anime = Anime(
                    malId = it.malId,
                    title = it.title,
                    episodes = it.episodes,
                    score = it.score,
                    imageUrl = it.images?.jpg?.imageUrl,
                    synopsis = it.synopsis,
                    trailerYoutubeId = it.trailer?.url,
                    genresCsv = it.genres?.mapNotNull { g -> g.name }?.joinToString(", "),
                    castCsv = it.characters?.mapNotNull { c -> c.name }?.joinToString(", ")
                )
                dao.insert(anime)
            }
        }
    }
}

fun AnimeDto.safeToAnime(): Anime {
    val image = this.images.jpg?.imageUrl
    return Anime(
        malId = this.malId,
        title = this.title,
        episodes = this.episodes,
        score = this.score,
        imageUrl = image,
        synopsis = this.synopsis
    )
}
