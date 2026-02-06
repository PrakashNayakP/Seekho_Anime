package com.example.seekhoanime.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeDetailResponse(
    @SerializedName("data") val data: AnimeDetailDto
)

data class AnimeDetailDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("images") val images: ImagesDto?,
    @SerializedName("episodes") val episodes: Int?,
    @SerializedName("score") val score: Double?,
    @SerializedName("synopsis") val synopsis: String?,
    @SerializedName("trailer") val trailer: TrailerDto?,
    @SerializedName("genres") val genres: List<GenreDto>?,
    @SerializedName("studios") val studios: List<StudioDto>?,
    @SerializedName("characters") val characters: List<CharacterDto>?
)

data class TrailerDto(
    @SerializedName("youtube_id") val youtubeId: String?,
    @SerializedName("embed_url") val url: String?
)

data class GenreDto(
    @SerializedName("mal_id") val malId: Int?,
    @SerializedName("name") val name: String?
)

data class StudioDto(
    @SerializedName("mal_id") val malId: Int?,
    @SerializedName("name") val name: String?
)

data class CharacterDto(
    @SerializedName("mal_id") val malId: Int?,
    @SerializedName("name") val name: String?
)

