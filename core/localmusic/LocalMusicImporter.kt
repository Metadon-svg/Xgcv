package com.simpmusic.localmusic

import android.content.ContentResolver
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object LocalMusicImporter {
    suspend fun importContentUri(context: Context, repository: LocalMusicRepository, uri: Uri): SongEntity {
        return withContext(Dispatchers.IO) {
            val resolver: ContentResolver = context.contentResolver
            // Try to extract metadata
            val mmr = MediaMetadataRetriever()
            try {
                resolver.openFileDescriptor(uri, "r")?.fileDescriptor?.let { fd ->
                    mmr.setDataSource(fd)
                } ?: mmr.setDataSource(uri.toString())

                val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                val artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                val album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                val durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val durationMs = durationStr?.toLongOrNull()
                val mimeType = resolver.getType(uri)
                // Note: album art extraction can be done, but we'll leave artUri null for now
                repository.importFromUriAndIndex(uri.toString(), mimeType, title, artist, album, durationMs, artUri = null)
            } finally {
                mmr.release()
            }
        }
    }
}