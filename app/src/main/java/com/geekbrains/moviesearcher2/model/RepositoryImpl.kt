package com.geekbrains.moviesearcher2.model

class RepositoryImpl : Repository {
    override fun getMovieFromServer() = Movie()
    override fun getMovieFromLocalStorage() = Movie()
}