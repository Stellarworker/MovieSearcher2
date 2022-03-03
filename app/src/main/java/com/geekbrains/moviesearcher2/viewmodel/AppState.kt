package com.geekbrains.moviesearcher2.viewmodel

import com.geekbrains.moviesearcher2.model.MovieDetails
import com.geekbrains.moviesearcher2.model.MoviesDTO

sealed class AppState {
    data class Success(val movieData: MoviesDTO) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}

sealed class AppStateDetails : AppState() {
    data class Success(val details: MovieDetails) : AppStateDetails()
    data class Error(val error: Throwable) : AppStateDetails()
    object Loading : AppStateDetails()
}