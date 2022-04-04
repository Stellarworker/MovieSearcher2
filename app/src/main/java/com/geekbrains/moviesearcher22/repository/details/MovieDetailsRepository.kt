package com.geekbrains.moviesearcher22.repository.details

import com.geekbrains.moviesearcher22.model.MovieDetails

interface MovieDetailsRepository {
    fun getMovieDetailsFromServer(
        id: Int,
        callback: retrofit2.Callback<MovieDetails>
    )
}