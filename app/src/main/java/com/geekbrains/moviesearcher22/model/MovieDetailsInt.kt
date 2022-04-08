package com.geekbrains.moviesearcher22.model

import com.geekbrains.moviesearcher22.common.EMPTY_DOUBLE
import com.geekbrains.moviesearcher22.common.EMPTY_INT
import com.geekbrains.moviesearcher22.common.EMPTY_LONG
import com.geekbrains.moviesearcher22.common.EMPTY_STRING

data class MovieDetailsInt(
    val movieId: Int = EMPTY_INT,
    val title: String = EMPTY_STRING,
    val posterPath: String = EMPTY_STRING,
    val tagLine: String = EMPTY_STRING,
    val genres: String = EMPTY_STRING,
    val releaseDate: String = EMPTY_STRING,
    val voteAverage: Double = EMPTY_DOUBLE,
    var viewTime: Long = EMPTY_LONG,
    var note: String = EMPTY_STRING
)