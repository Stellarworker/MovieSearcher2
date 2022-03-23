package com.geekbrains.moviesearcher2.viewmodel

import com.geekbrains.moviesearcher2.model.MovieDetailsInt

sealed class AppStateHistory {
    data class Success(val movieDetailsInt: List<MovieDetailsInt>) : AppStateHistory()
    data class Error(val error: Throwable) : AppStateHistory()
    object Loading : AppStateHistory()
}