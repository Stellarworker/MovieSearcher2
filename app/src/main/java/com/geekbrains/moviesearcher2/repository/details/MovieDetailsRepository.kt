package com.geekbrains.moviesearcher2.repository.details

import com.geekbrains.moviesearcher2.model.MovieDetails

interface MovieDetailsRepository {
    fun getMovieDetailsFromServer(
        id: Int,
        callback: retrofit2.Callback<MovieDetails>
    )
}