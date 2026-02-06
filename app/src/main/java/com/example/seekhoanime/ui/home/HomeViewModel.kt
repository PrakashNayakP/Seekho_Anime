package com.example.seekhoanime.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seekhoanime.data.repository.AnimeRepository
import com.example.seekhoanime.model.Anime
import com.example.seekhoanime.util.ConnectivityObserver
import com.example.seekhoanime.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AnimeRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val connectivity = ConnectivityObserver(context)
    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private var currentPage = 1
    private var isRefreshingApiCalled=false

    init {
        observeAnimeData()
        observeConnectivity()
    }

    fun observeAnimeData(){
        viewModelScope.launch {
            repository.getTopAnime()
                .catch { e ->
                    _uiEvent.emit(HomeUiEvent.ShowError(e.message ?: "An error occurred"))
                }
                .collect { list ->
                    _animeList.value = list
                }
        }
    }

    fun observeConnectivity() {
        viewModelScope.launch {
            connectivity.observe().collect { status ->
                when (status) {
                    is Status.Available -> {
                        if(!isRefreshingApiCalled) {
                            isRefreshingApiCalled = true
                            refresh()
                        }
                    }
                    is Status.Unavailable -> {
                        _uiEvent.emit(HomeUiEvent.ShowError("No internet connection"))
                    }
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _loading.value = true
            try {
                currentPage = 1
                repository.refreshTopAnime()
            } catch (e: Exception) {
                _uiEvent.emit(HomeUiEvent.ShowError(e.message ?: "An error occurred"))

            } finally {
                _loading.value = false
            }
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            _loading.value = true
            try {
                currentPage += 1
                repository.refreshTopAnimePage(currentPage)
            } catch (e: Exception) {
                _uiEvent.emit(HomeUiEvent.ShowError(e.message ?: "An error occurred"))
                currentPage -= 1
            } finally {
                _loading.value = false
            }
        }
    }
}
