package com.example.seekhoanime.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.seekhoanime.model.Anime
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Query("SELECT * FROM anime ORDER BY score DESC")
    fun getAllAnime(): Flow<List<Anime>>

    @Query("SELECT * FROM anime WHERE malId = :id LIMIT 1")
    fun getAnimeById(id: Int): Flow<Anime?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(anime: List<Anime>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anime: Anime)

    @Query("DELETE FROM anime")
    suspend fun clearAll()
}

