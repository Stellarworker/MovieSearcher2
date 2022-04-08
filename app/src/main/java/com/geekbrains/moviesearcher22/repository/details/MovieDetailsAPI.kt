package com.geekbrains.moviesearcher22.repository.details

import com.geekbrains.moviesearcher22.model.MovieDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val URL = "https://api.themoviedb.org/3/movie/{id}"
private const val ID = "id"
private const val API_KEY = "api_key"

interface MovieDetailsAPI {
    @GET(URL)
    fun getMovieDetails(
        @Path(ID) id: Int,
        @Query(API_KEY) apiKey: String
    ): Call<MovieDetails>
}