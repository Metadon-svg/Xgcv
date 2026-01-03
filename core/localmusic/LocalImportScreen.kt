package com.simpmusic.ui.library

import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri

@Composable
fun LocalImportScreen(viewModel: LocalImportViewModel) {
    val importing = viewModel.importing.collectAsState(initial = false)
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            viewModel.importUris(uris)
        }
    }

    if (importing.value) {
        CircularProgressIndicator()
    } else {
        Button(onClick = {
            // allow mp3 and common audio mime types
            launcher.launch(arrayOf("audio/mpeg", "audio/*"))
        }) {
            Text("Import local files")
        }
    }
}