package com.geekbrains.moviesearcher22.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.geekbrains.moviesearcher22.common.*

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = ZERO_LONG,
    val movieId: Int = EMPTY_INT,
    val title: String = EMPTY_STRING,
    val posterPath: String = EMPTY_STRING,
    val tagLine: String = EMPTY_STRING,
    val genres: String = EMPTY_STRING,
    val releaseDate: String = EMPTY_STRING,
    val voteAverage: Double = EMPTY_DOUBLE,
    val viewTime: Long = EMPTY_LONG,
    val note: String = EMPTY_STRING
)