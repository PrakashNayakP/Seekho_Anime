package com.example.seekhoanime.ui.detail

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun DetailScreen(viewModel: DetailViewModel, onBack: () -> Unit) {
    val anime by viewModel.anime.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        anime?.let { a ->
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "← Back", modifier = Modifier
                    .clickable { onBack() }
                    .padding(4.dp), style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(8.dp))

                val embedUrl = a.trailerYoutubeId
                val youtubeId = embedUrl?.let { extractYoutubeId(it) }

                if (!youtubeId.isNullOrEmpty()) {

                    YoutubeTrailerPlayer(
                        youtubeId = youtubeId,
                        posterUrl = a.imageUrl,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )

                } else {

                    val painter = rememberAsyncImagePainter(a.imageUrl)

                    Image(
                        painter = painter,
                        contentDescription = a.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = a.title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Episodes: ${a.episodes ?: "?"}")
                Text(text = "Rating: ${a.score ?: "?"}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = a.synopsis ?: "No synopsis available.")

                Spacer(modifier = Modifier.height(8.dp))
                a.genresCsv?.let { Text(text = "Genres: $it") }
                a.castCsv?.let { Text(text = "Cast: $it") }
            }
        }

        error?.let { err ->
            Toast.makeText(LocalContext.current, err, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun YoutubeTrailerPlayer(
    youtubeId: String?,
    posterUrl: String?,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var playerState by remember { mutableStateOf<TrailerPlayerState>(TrailerPlayerState.Loading) }

    Box(modifier = modifier) {

        if (youtubeId != null && playerState != TrailerPlayerState.Error) {

            AndroidView(
                factory = { ctx ->

                    YouTubePlayerView(ctx).apply {

                        lifecycleOwner.lifecycle.addObserver(this)

                        addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

                            override fun onReady(player: YouTubePlayer) {
                                playerState = TrailerPlayerState.Ready
                                player.cueVideo(youtubeId, 0f)
                            }

                            override fun onError(
                                player: YouTubePlayer,
                                error: PlayerConstants.PlayerError
                            ) {
                                playerState = TrailerPlayerState.Error
                            }
                        })
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        if (playerState == TrailerPlayerState.Loading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }

        if (playerState == TrailerPlayerState.Error) {


            posterUrl?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Trailer preview",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    "Trailer unavailable inside app",
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=$youtubeId")
                        )
                        context.startActivity(intent)
                    }
                ) {
                    Text("Watch on YouTube")
                }
            }
        }
    }
}




fun extractYoutubeId(url: String?): String? {
    if (url.isNullOrEmpty()) return null

    val regex = Regex(
        "(?:youtube\\.com/(?:embed/|watch\\?v=)|youtu\\.be/|youtube-nocookie\\.com/embed/)([a-zA-Z0-9_-]{11})"
    )

    return regex.find(url)?.groupValues?.get(1)
}

sealed class TrailerPlayerState {
    object Loading : TrailerPlayerState()
    object Ready : TrailerPlayerState()
    object Error : TrailerPlayerState()
}
