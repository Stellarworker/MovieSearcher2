package com.geekbrains.moviesearcher22.viewmodel

import com.geekbrains.moviesearcher22.model.MoviesDTO

sealed class AppState {
    data class Success(val movieData: MoviesDTO) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
