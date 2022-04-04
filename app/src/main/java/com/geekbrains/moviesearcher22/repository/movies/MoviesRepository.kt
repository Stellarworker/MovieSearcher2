package com.geekbrains.moviesearcher22.repository.movies

import com.geekbrains.moviesearcher22.model.MoviesDTO

interface MoviesRepository {
    fun getMoviesFromServer(
        includeAdult: Boolean,
        query: String,
        callback: retrofit2.Callback<MoviesDTO>
    )
}