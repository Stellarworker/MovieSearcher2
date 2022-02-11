package com.geekbrains.moviesearcher2.model

data class Movie(
    val title: String = "The Terminator",
    val genre: String = "Thriller",
    val releaseYear: Int = 1984,
    val userScore: Int = 76
)