package com.geekbrains.moviesearcher2.viewmodel

import com.geekbrains.moviesearcher2.model.MovieDetailsInt

sealed class AppStateDetails {
    data class Success(val movieDetails: MovieDetailsInt) : AppStateDetails()
    data class Error(val error: Throwable) : AppStateDetails()
    object Loading : AppStateDetails()
}