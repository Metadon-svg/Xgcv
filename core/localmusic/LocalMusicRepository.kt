package com.simpmusic.localmusic

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class LocalMusicRepository(
    private val context: Context,
    private val db: LocalMusicDatabase
) {
    private val songDao = db.songDao()

    suspend fun importFileAndIndex(sourceFilePath: String, mimeType: String?, title: String?, artist: String?, album: String?, durationMs: Long?, artUri: String?): SongEntity {
        return withContext(Dispatchers.IO) {
            // Copy file into app files dir under "local_music"
            val libDir = File(context.filesDir, "local_music").apply { if (!exists()) mkdirs() }
            val dest = File(libDir, UUID.randomUUID().toString() + "_" + File(sourceFilePath).name)
            File(sourceFilePath).copyTo(dest, overwrite = false)

            val song = SongEntity(
                id = UUID.randomUUID().toString(),
                title = title,
                artist = artist,
                album = album,
                durationMs = durationMs,
                filePath = dest.absolutePath,
                mimeType = mimeType,
                artUri = artUri
            )

            songDao.upsert(song)
            song
        }
    }

    suspend fun importFromUriAndIndex(uriString: String, mimeType: String?, title: String?, artist: String?, album: String?, durationMs: Long?, artUri: String?): SongEntity {
        // For SAF/Content URIs — copy via content resolver to app storage (left as example)
        return withContext(Dispatchers.IO) {
            val srcUri = android.net.Uri.parse(uriString)
            val libDir = File(context.filesDir, "local_music").apply { if (!exists()) mkdirs() }
            val dest = File(libDir, UUID.randomUUID().toString() + ".mp3")
            context.contentResolver.openInputStream(srcUri).use { input ->
                dest.outputStream().use { out -> input?.copyTo(out) }
            }

            val song = SongEntity(
                id = UUID.randomUUID().toString(),
                title = title,
                artist = artist,
                album = album,
                durationMs = durationMs,
                filePath = dest.absolutePath,
                mimeType = mimeType,
                artUri = artUri
            )

            songDao.upsert(song)
            song
        }
    }
}