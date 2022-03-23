package com.geekbrains.moviesearcher2.model

data class MovieDetailsInt(
    val movieId: Int = -1,
    val title: String = "",
    val posterPath: String = "",
    val tagLine: String = "",
    val genres: String = "",
    val releaseDate: String = "",
    val voteAverage: Double = 0.0,
    var viewTime: Long = -1,
    val note: String = ""
)