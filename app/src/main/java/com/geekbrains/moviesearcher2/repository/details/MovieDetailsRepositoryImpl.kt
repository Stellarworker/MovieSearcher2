package com.geekbrains.moviesearcher2.repository.details

import com.geekbrains.moviesearcher2.model.MovieDetails

class MovieDetailsRepositoryImpl(
    private val movieDetailsRemoteDataSource: MovieDetailsRemoteDataSource
) : MovieDetailsRepository {
    override fun getMovieDetailsFromServer(
        id: Int,
        callback: retrofit2.Callback<MovieDetails>
    ) {
        movieDetailsRemoteDataSource.getMovieDetails(id, callback)
    }
}