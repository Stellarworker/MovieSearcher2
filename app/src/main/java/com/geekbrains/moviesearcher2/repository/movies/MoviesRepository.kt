package com.geekbrains.moviesearcher2.repository.movies

import com.geekbrains.moviesearcher2.model.MoviesDTO

interface MoviesRepository {
    fun getMoviesFromServer(
        includeAdult: Boolean,
        query: String,
        callback: retrofit2.Callback<MoviesDTO>
    )
}