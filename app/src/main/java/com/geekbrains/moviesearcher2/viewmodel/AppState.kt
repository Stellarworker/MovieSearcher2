package com.geekbrains.moviesearcher2.viewmodel

import com.geekbrains.moviesearcher2.model.Movie

sealed class AppState {
    data class Success(val movieData: List<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}