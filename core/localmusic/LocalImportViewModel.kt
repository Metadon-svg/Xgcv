package com.simpmusic.ui.library

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.simpmusic.localmusic.LocalMusicImporter
import com.simpmusic.localmusic.LocalMusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocalImportViewModel(
    application: Application,
    private val repository: LocalMusicRepository
) : AndroidViewModel(application) {
    private val _importing = MutableStateFlow(false)
    val importing: StateFlow<Boolean> = _importing

    fun importUris(uris: List<Uri>) {
        viewModelScope.launch {
            _importing.value = true
            for (uri in uris) {
                try {
                    LocalMusicImporter.importContentUri(getApplication(), repository, uri)
                } catch (e: Exception) {
                    // handle / log import error
                }
            }
            _importing.value = false
        }
    }
}