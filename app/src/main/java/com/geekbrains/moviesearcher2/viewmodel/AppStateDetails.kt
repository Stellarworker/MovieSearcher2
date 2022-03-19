package com.geekbrains.moviesearcher2.viewmodel

import com.geekbrains.moviesearcher2.model.MovieDetails

sealed class AppStateDetails {
    data class Success(val movieDetails: MovieDetails) : AppStateDetails()
    data class Error(val error: Throwable) : AppStateDetails()
    object Loading : AppStateDetails()
}