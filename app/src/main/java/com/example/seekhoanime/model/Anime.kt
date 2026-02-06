package com.example.seekhoanime.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime")
data class Anime(
    @PrimaryKey val malId: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val imageUrl: String?,
    val synopsis: String?,
    val trailerYoutubeId: String? = null,
    val genresCsv: String? = null,
    val castCsv: String? = null
)

