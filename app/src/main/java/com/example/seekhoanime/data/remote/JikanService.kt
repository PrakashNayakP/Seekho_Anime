package com.example.seekhoanime.data.remote

import com.example.seekhoanime.data.remote.dto.TopAnimeResponse
import com.example.seekhoanime.data.remote.dto.AnimeDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanService {
    @GET("/v4/top/anime")
    suspend fun getTopAnime(@Query("page") page: Int = 1): Response<TopAnimeResponse>

    @GET("/v4/anime/{id}")
    suspend fun getAnimeDetail(@Path("id") id: Int): Response<AnimeDetailResponse>
}
