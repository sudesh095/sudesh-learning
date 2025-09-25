package com.sudesh.learning.presentation.player

import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView
import com.sudesh.core.Constants

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    url: String = Constants.VIDEO_URL,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val playerUrl by rememberSaveable { mutableStateOf(url) }
    Log.e("URL -> ", " from -> $url")
    Log.e("URL -> ", " player -> $playerUrl")

    // Track selector for ABR
    val trackSelector = remember {
        DefaultTrackSelector(context).apply {
            setParameters(buildUponParameters().setForceHighestSupportedBitrate(false))
        }
    }

    // ExoPlayer instance
    val player = remember {
        ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
            .apply {
                // Step 2: MediaItem (HLS sample)
                val mediaItem = MediaItem.Builder()
                    .setUri(playerUrl)
//                    .setMimeType(MimeTypes.APPLICATION_M3U8) // important for HLS
                    .build()
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    val isLowInternet by viewModel.isLowInternet.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.monitorNetwork()
    }

    var showError by remember { mutableStateOf(false) }

    // Error listener
    LaunchedEffect(player) {
        player.addListener(object : androidx.media3.common.Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                showError = true
            }
        })
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    useController = false // Disable default controls
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Custom Overlay Controls
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0x80000000))
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { player.seekBack() }) { Text("⏪") }
                Button(onClick = {
                    if (player.isPlaying) player.pause() else player.play()
                }) { Text(if (player.isPlaying) "⏸" else "▶") }
                Button(onClick = { player.seekForward() }) { Text("⏩") }
            }
        }

        // Error Retry UI
        if (showError) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Playback Error", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        showError = false
                        player.prepare()
                        player.playWhenReady = true
                    }) {
                        Text("Retry")
                    }
                }
            }
        }

        // Low Internet Snackbar
        if (isLowInternet) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(8.dp),
                containerColor = Color.Red
            ) {
                Text("⚠ Low internet connection")
            }
        }
    }
}