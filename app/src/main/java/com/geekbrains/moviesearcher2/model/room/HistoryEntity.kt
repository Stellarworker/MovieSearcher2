package com.geekbrains.moviesearcher2.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val movieId: Int = -1,
    val title: String = "",
    val posterPath: String = "",
    val tagLine: String = "",
    val genres: String = "",
    val releaseDate: String = "",
    val voteAverage: Double = 0.0,
    val viewTime: Long = -1,
    val note: String = ""
)