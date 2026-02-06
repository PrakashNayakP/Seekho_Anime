package com.example.seekhoanime.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TopAnimeResponse(
    @SerializedName("data") val data: List<AnimeDto>
)

data class AnimeDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("images") val images: ImagesDto,
    @SerializedName("episodes") val episodes: Int?,
    @SerializedName("score") val score: Double?,
    @SerializedName("synopsis") val synopsis: String?
)

data class ImagesDto(
    @SerializedName("jpg") val jpg: ImageJpgDto?
)

data class ImageJpgDto(
    @SerializedName("image_url") val imageUrl: String?
)

