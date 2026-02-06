package com.example.seekhoanime.ui.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.seekhoanime.model.Anime

@Composable
fun HomeScreen(viewModel: HomeViewModel, onAnimeClick: (Int) -> Unit) {
    val animeList by viewModel.animeList.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is HomeUiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(animeList) { anime ->
                AnimeRow(anime = anime, onClick = { onAnimeClick(anime.malId) })
            }

            if(animeList.isNotEmpty() && !loading) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.loadNextPage() },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Text(text = "Load more")
                    }
                }
            }
        }
    }
}

@Composable
fun AnimeRow(anime: Anime, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onClick() }) {

        val painter = rememberAsyncImagePainter(anime.imageUrl)
        Image(
            painter = painter,
            contentDescription = anime.title,
            modifier = Modifier.size(96.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = anime.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "Episodes: ${anime.episodes ?: "?"}")
            Text(text = "Rating: ${anime.score ?: "?"}")
        }
    }
}

sealed interface HomeUiState {

    data object Loading : HomeUiState

    data class Success(
        val animeList: List<Anime>
    ) : HomeUiState

    data class Error(
        val message: String
    ) : HomeUiState
}

sealed interface HomeUiEvent {
    data class ShowError(val message: String) : HomeUiEvent
}
