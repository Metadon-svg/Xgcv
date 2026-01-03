package com.simpmusic.localmusic

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM local_songs ORDER BY title")
    fun observeAll(): Flow<List<SongEntity>>

    @Query("SELECT * FROM local_songs WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): SongEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(song: SongEntity)

    @Query("DELETE FROM local_songs WHERE id = :id")
    suspend fun deleteById(id: String)
}