package com.geekbrains.moviesearcher2.repository.movies

import com.geekbrains.moviesearcher2.model.MoviesDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesAPI {
    @GET("https://api.themoviedb.org/3/search/movie")
    fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("query") query: String,
    ): Call<MoviesDTO>
}