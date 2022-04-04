package com.geekbrains.moviesearcher22.repository.details

import com.geekbrains.moviesearcher22.model.MovieDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDetailsAPI {
    @GET("https://api.themoviedb.org/3/movie/{id}")
    fun getMovieDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieDetails>
}