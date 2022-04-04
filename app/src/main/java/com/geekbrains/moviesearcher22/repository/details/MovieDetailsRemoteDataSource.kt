package com.geekbrains.moviesearcher22.repository.details

import com.geekbrains.moviesearcher22.BuildConfig
import com.geekbrains.moviesearcher22.model.MovieDetails
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDetailsRemoteDataSource {
    private val movieDetailsApi = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/search/movie/")
        .addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().setLenient().create())
        )
        .build().create(MovieDetailsAPI::class.java)

    fun getMovieDetails(id: Int, callback: Callback<MovieDetails>) {
        movieDetailsApi.getMovieDetails(id, BuildConfig.THEMOVIEDB_API_KEY).enqueue(callback)
    }
}