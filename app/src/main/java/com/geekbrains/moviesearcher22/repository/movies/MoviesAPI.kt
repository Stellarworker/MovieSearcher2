package com.geekbrains.moviesearcher22.repository.movies

import com.geekbrains.moviesearcher22.model.MoviesDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val URL = "https://api.themoviedb.org/3/search/movie"
private const val API_KEY = "api_key"
private const val INCLUDE_ADULT = "include_adult"
private const val QUERY = "query"

interface MoviesAPI {
    @GET(URL)
    fun getMovies(
        @Query(API_KEY) apiKey: String,
        @Query(INCLUDE_ADULT) includeAdult: Boolean,
        @Query(QUERY) query: String,
    ): Call<MoviesDTO>
}