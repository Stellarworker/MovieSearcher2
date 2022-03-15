package com.geekbrains.moviesearcher2.viewmodel

import com.geekbrains.moviesearcher2.model.MoviesDTO

sealed class AppState {
    data class Success(val movieData: MoviesDTO) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
