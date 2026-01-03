package com.simpmusic.localmusic

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SongEntity::class], version = 1)
abstract class LocalMusicDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao()
}