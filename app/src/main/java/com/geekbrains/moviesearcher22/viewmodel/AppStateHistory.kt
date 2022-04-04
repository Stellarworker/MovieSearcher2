package com.geekbrains.moviesearcher22.viewmodel

import com.geekbrains.moviesearcher22.model.MovieDetailsInt

sealed class AppStateHistory {
    data class Success(val movieDetailsInt: List<MovieDetailsInt>) : AppStateHistory()
    data class Error(val error: Throwable) : AppStateHistory()
    object Loading : AppStateHistory()
}