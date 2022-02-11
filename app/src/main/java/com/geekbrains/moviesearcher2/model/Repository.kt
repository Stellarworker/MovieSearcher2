package com.geekbrains.moviesearcher2.model

interface Repository {
    fun getMovieFromServer(): Movie
    fun getMovieFromLocalStorage(): Movie
}