package com.geekbrains.moviesearcher2.model

interface Repository {
    fun getMoviesFromServer(query: String): List<Movie>
}