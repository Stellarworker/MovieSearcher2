package com.geekbrains.moviesearcher22.repository.movies

import com.geekbrains.moviesearcher22.BuildConfig
import com.geekbrains.moviesearcher22.model.MoviesDTO
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MoviesRemoteDataSource {
    private val moviesApi = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/search/movie/")
        .addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().setLenient().create())
        )
        .build().create(MoviesAPI::class.java)

    fun getMovies(includeAdult: Boolean, query: String, callback: Callback<MoviesDTO>) {
        moviesApi.getMovies(BuildConfig.THEMOVIEDB_API_KEY, includeAdult, query).enqueue(callback)
    }
}